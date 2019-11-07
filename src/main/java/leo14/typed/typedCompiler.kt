package leo14.typed

import leo.base.notNullIf
import leo.base.notNullOrError
import leo13.Stack
import leo13.mapFirst
import leo13.push
import leo14.*
import leo14.lambda.fn
import leo14.lambda.invoke
import leo14.lambda.term

fun <T> typedCompiler(stack: Stack<Function<T>>, lit: (Literal) -> T, ret: Ret<Typed<T>>): Compiler =
	emptyTyped<T>().plusCompiler(stack, lit, ret)

fun <T> Typed<T>.plusCompiler(stack: Stack<Function<T>>, lit: (Literal) -> T, ret: Ret<Typed<T>>): Compiler =
	compiler { token ->
		when (token) {
			is LiteralToken ->
				plus(term(lit(token.literal)) of nativeLine)
					.plusCompiler(stack, lit, ret)
			is BeginToken ->
				when (token.begin.string) {
					"let" ->
						beginCompiler("it") {
							typeCompiler { param ->
								beginCompiler("be") {
									typedCompiler(stack, lit) { body ->
										endCompiler {
											plusCompiler(stack.push(param ret body), lit, ret)
										}
									}
								}
							}
						}
					"function" ->
						beginCompiler("takes") {
							typeCompiler { param ->
								beginCompiler("gives") {
									typedCompiler(stack, lit) { body ->
										endCompiler {
											plus(fn(body.term) of line(param arrowTo body.type))
												.plusCompiler(stack, lit, ret)
										}
									}
								}
							}
						}
					"give" ->
						typedCompiler(stack, lit) { param ->
							type.onlyLineOrNull?.arrowOrNull?.let { arrow ->
								if (arrow.lhs != param.type) error("${param.type} as ${arrow.lhs}")
								else term.invoke(param.term).of(arrow.rhs).plusCompiler(stack, lit, ret)
							}.notNullOrError("$type as function")
						}
					else ->
						typedCompiler(stack, lit) { rhs ->
							resolve(stack, rhs.term of (token.begin.string fieldTo rhs.type))
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