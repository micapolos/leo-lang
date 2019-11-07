package leo14.typed

import leo.base.notNullIf
import leo.base.notNullOrError
import leo13.Stack
import leo13.mapFirstIndexed
import leo13.push
import leo14.*
import leo14.lambda.*

fun <T> typedCompiler(stack: Stack<Arrow>, lit: (Literal) -> T, ret: Ret<Typed<T>>): Compiler =
	emptyTyped<T>().plusCompiler(stack, lit, ret)

fun <T> Typed<T>.plusCompiler(stack: Stack<Arrow>, lit: (Literal) -> T, ret: Ret<Typed<T>>): Compiler =
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
											plusCompiler(stack.push(param arrowTo body.type), lit) { typed ->
												ret(fn(arg0<T>().invoke(typed.term)).invoke(fn(body.term)) of typed.type)
											}
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
					"match" -> TODO()
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

fun <T> Typed<T>.resolve(stack: Stack<Arrow>, typed: TypedField<T>): Typed<T> =
	plus(typed).let { plused ->
		plused.resolve(stack) ?: resolve(typed) ?: plused
	}

fun <T> Typed<T>.resolve(stack: Stack<Arrow>): Typed<T>? =
	stack
		.mapFirstIndexed { resolve(this) }
		?.let { indexToType ->
			term(variable<T>(indexToType.first)).invoke(term) of indexToType.second
		}

fun <T> Typed<T>.resolve(arrow: Arrow): Type? =
	notNullIf(type == arrow.lhs) {
		arrow.rhs
	}
