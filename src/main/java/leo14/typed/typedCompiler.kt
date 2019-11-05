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
						emptyType.plusCompiler { param ->
							beginCompiler("gives") {
								emptyTyped<T>().plusCompiler(stack, lit) { body ->
									endCompiler {
										plusCompiler(stack.push(param ret body), lit, ret)
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
							resolve(stack, token.begin.string, rhsTyped).plusCompiler(stack, lit, ret)
						}
				}
			is EndToken ->
				ret(this)
		}
	}

fun <T> Typed<T>.resolve(stack: Stack<Function<T>>, string: String, rhs: Typed<T>): Typed<T> =
	plus(string, rhs).let { plused ->
		plused.resolve(stack) ?: resolve(string, rhs) ?: plused
	}

fun <T> Typed<T>.resolve(stack: Stack<Function<T>>): Typed<T>? =
	stack.mapFirst { resolve(this) }

fun <T> Typed<T>.resolve(function: Function<T>): Typed<T>? =
	notNullIf(type == function.param) {
		term.invoke(function.body.term) of function.body.type
	}