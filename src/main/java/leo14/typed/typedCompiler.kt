package leo14.typed

import leo.base.notNullIf
import leo.base.notNullOrError
import leo.base.orIfNull
import leo13.*
import leo14.*
import leo14.lambda.*

fun <T> typedCompiler(stack: Stack<Arrow>, lit: (Literal) -> T, ret: Ret<Typed<T>>): Compiler =
	typed<T>().plusCompiler(stack, lit, ret)

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
						onlyLine.choice.plusMatchCompiler(stack, lit) { typed ->
							typed.plusCompiler(stack, lit, ret)
						}
					"of" ->
						typeCompiler { type ->
							of(type).plusCompiler(stack, lit, ret)
						}
					"scope" ->
						endCompiler {
							script("scope" lineTo stack.script { scriptLine })
								.staticTyped<T>()
								.plusCompiler(stack, lit, ret)
						}
					"delete" ->
						endCompiler {
							typedCompiler(stack, lit, ret)
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
	typed<T>().plusCompilerWith(function, stack, lit, ret)

fun <T> TypedChoice<T>.plusMatchCompiler(stack: Stack<Arrow>, lit: (Literal) -> T, ret: Ret<Typed<T>>): Compiler =
	Match(term, choice.optionStack.reverse, null).plusCompiler(stack, lit, ret)

fun <T> Match<T>.plusCompiler(stack: Stack<Arrow>, lit: (Literal) -> T, ret: Ret<Typed<T>>): Compiler =
	when (optionStack) {
		is EmptyStack ->
			if (inferredTypeOrNull == null) error("impossible")
			else endCompiler { ret(term of inferredTypeOrNull) }
		is LinkStack ->
			term.of(optionStack.link.value)
				.plusOptionCompiler(inferredTypeOrNull, stack, lit) { typed ->
					Match(typed.term, optionStack.link.stack, typed.type).plusCompiler(stack, lit, ret)
				}
	}

fun <T> TypedOption<T>.plusOptionCompiler(
	expectedType: Type?,
	stack: Stack<Arrow>,
	lit: (Literal) -> T,
	ret: Ret<Typed<T>>): Compiler =
	beginCompiler(option.string) {
		arg0<T>()
			.of(option.rhs)
			.plusCompiler(stack, lit) { typed ->
				ret(term.invoke(typed.term) of
					expectedType
						?.apply { typed.type.checkIs(this) }
						.orIfNull { typed.type })
			}
	}