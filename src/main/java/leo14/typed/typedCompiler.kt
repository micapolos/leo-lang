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
						typeCompiler { param ->
							beginCompiler("give") {
								typedCompiler(stack, lit) { body ->
									plusCompilerWith(param ret body, stack, lit, ret)
								}
							}
						}
					"remember" ->
						endCompiler {
							beginCompiler("as") {
								typeCompiler { name ->
									typedCompilerWith(name ret this, stack, lit, ret)
								}
							}
						}
					"forget" -> TODO() // forget: type && forget: everything
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
					"match" ->
						type.onlyLineOrNull?.choiceOrNull?.let { choice ->
							plusMatchCompiler(choice, stack, lit, ret)
						} ?: error("$type as choice")
					"of" ->
						typeCompiler { type ->
							castTypedTo(type).plusCompiler(stack, lit, ret)
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

fun <T> Typed<T>.plusCompilerWith(function: Function<T>, stack: Stack<Arrow>, lit: (Literal) -> T, ret: Ret<Typed<T>>): Compiler =
	plusCompiler(stack.push(function.param arrowTo function.body.type), lit) { typed ->
		ret(fn(arg0<T>().invoke(typed.term)).invoke(fn(function.body.term)) of typed.type)
	}

fun <T> typedCompilerWith(function: Function<T>, stack: Stack<Arrow>, lit: (Literal) -> T, ret: Ret<Typed<T>>): Compiler =
	emptyTyped<T>().plusCompilerWith(function, stack, lit, ret)

fun <T> Typed<T>.plusMatchCompiler(choice: Choice, stack: Stack<Arrow>, lit: (Literal) -> T, ret: Ret<Typed<T>>): Compiler =
	TODO()
//	choice.previousChoiceOrNull?.let { previousChoice ->
//		plusMatchCompiler(previousChoice, stack, lit) { previousTyped ->
//			plusCaseCompiler(choice.lastField, previousTyped.type, stack, lit, ret)
//		}
//	}.orIfNull {
//		plusCaseCompiler(choice.lastField, null, stack, lit, ret)
//	}
//
//fun <T> Typed<T>.plusCaseCompiler(
//	case: Field,
//	expectedType: Type?,
//	stack: Stack<Arrow>,
//	lit: (Literal) -> T,
//	ret: Ret<Typed<T>>): Compiler =
//	beginCompiler(case.string) {
//		typedCompilerWith(type("matching") ret (arg0<T>() of case.rhs), stack, lit) { typed ->
//			ret(term.invoke(typed.term) of
//				expectedType
//					?.apply { typed.type.checkIs(this) }
//					.orIfNull { typed.type })
//		}
//	}