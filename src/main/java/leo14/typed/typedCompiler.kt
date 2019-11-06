package leo14.typed

import leo.base.notNullIf
import leo13.Stack
import leo13.mapFirst
import leo13.push
import leo14.*
import leo14.lambda.invoke
import leo14.lambda.term

fun <T> Typed<T>.plusCompiler(stack: Stack<Function<T>>, lit: (Literal) -> T, ret: Ret<Typed<T>>): Compiler =
	compiler { token ->
		when (token) {
			is LiteralToken ->
				plusNative(term(lit(token.literal))).plusCompiler(stack, lit, ret)
			is BeginToken ->
				when (token.begin.string) {
					"let" ->
						beginCompiler("it") {
							emptyType.plusCompiler { param ->
								beginCompiler("be") {
									emptyTyped<T>().plusCompiler(stack, lit) { body ->
										endCompiler {
											plusCompiler(stack.push(param ret body), lit, ret)
										}
									}
								}
							}
						}
					"function" ->
						TODO()
					"apply" ->
						TODO()
					else ->
						emptyTyped<T>().plusCompiler(stack, lit) { rhsTyped ->
							resolve(stack, rhsTyped.term of (token.begin.string fieldTo rhsTyped.type))
								.plusCompiler(stack, lit, ret)
						}
				}
			is EndToken ->
				ret(this)
		}
	}

fun <T> Typed<T>.resolve(stack: Stack<Function<T>>, typed: TypedField<T>): Typed<T> =
	plus(typed).let { plused ->
		plused.resolve(stack) ?: resolve(typed) ?: plused
	}

fun <T> Typed<T>.resolve(stack: Stack<Function<T>>): Typed<T>? =
	stack.mapFirst { resolve(this) }

fun <T> Typed<T>.resolve(function: Function<T>): Typed<T>? =
	notNullIf(type == function.param) {
		term.invoke(function.body.term) of function.body.type
	}