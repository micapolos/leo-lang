package leo14.typed.compiler

import leo.base.notNullOrError
import leo13.reverse
import leo14.*
import leo14.lambda.arg0
import leo14.lambda.invoke
import leo14.lambda.term
import leo14.typed.*

// === Script writing

fun <T> Compiler<T>.compile(script: Script): Compiler<T> =
	when (script) {
		is UnitScript -> this
		is LinkScript -> compile(script.link)
	}

fun <T> Compiler<T>.compile(link: ScriptLink): Compiler<T> =
	compile(link.lhs).compile(link.line)

fun <T> Compiler<T>.compile(line: ScriptLine): Compiler<T> =
	when (line) {
		is LiteralScriptLine -> compile(token(line.literal))
		is FieldScriptLine -> compile(line.field)
	}

fun <T> Compiler<T>.compile(field: ScriptField): Compiler<T> =
	compile(token(begin(field.string))).compile(field.rhs).compile(token(end))

// === Compiling

fun <T> Compiler<T>.compile(token: Token): Compiler<T> =
	when (this) {
		is TypedCompiler ->
			when (token) {
				is LiteralToken ->
					this.plus(term(context.compile(token.literal)) of nativeLine)
				is BeginToken ->
					when (token.begin.string) {
						// Remove when macros are implemented
						context.dictionary.give ->
							TypedCompiler(GiveParent(this), context, typed())
						context.dictionary.`as` ->
							TypeCompiler(AsParent(this), type())
						context.dictionary.match ->
							MatchCompiler(
								this,
								Match(
									typed.term,
									typed.onlyLine.choice.choice.optionStack.reverse,
									null))
						context.dictionary.action ->
							ActionCompiler(this)
						context.dictionary.remember ->
							RememberCompiler(this)
						context.dictionary.forget ->
							TypeCompiler(ForgetParent(this), type())
						context.dictionary.`do` ->
							typed.onlyLine.arrow.let { arrow ->
								TypedCompiler(DoParent(this, arrow), context, typed())
							}
						else ->
							TypedCompiler(BeginTypedParent(this, token.begin), context, typed())
					}
				is EndToken ->
					parent?.compile(this.retTyped)
			}
		is TypeCompiler ->
			when (token) {
				is LiteralToken ->
					null
				is BeginToken ->
					when (token.begin.string) {
						"native" ->
							NativeCompiler(this)
						"choice" ->
							ChoiceCompiler(this, choice())
						"arrow" ->
							TODO()
						else ->
							TypeCompiler(BeginTypeParent(this, token.begin), type())
					}
				is EndToken ->
					parent?.compile(type)
			}
		is NativeCompiler ->
			when (token) {
				is LiteralToken ->
					null
				is BeginToken ->
					null
				is EndToken ->
					parent.updateType { plus(nativeLine) }
			}
		is ChoiceCompiler ->
			when (token) {
				is LiteralToken ->
					null
				is BeginToken ->
					TypeCompiler(ChoiceTypeParent(this, token.begin), type())
				is EndToken ->
					parent.updateType { plus(line(choice)) }
			}
		is MatchCompiler ->
			when (token) {
				is LiteralToken ->
					null
				is BeginToken -> {
					val line = match.beginCase(token.begin.string)
					TypedCompiler(
						MatchTypedParent(MatchCompiler(parent, line.match)),
						parent.context,
						line.typed)
				}
				is EndToken ->
					parent.updateTyped { match.end() }
			}
		is ActionCompiler ->
			when (token) {
				is LiteralToken -> null
				is BeginToken ->
					when (token.begin.string) {
						parent.context.dictionary.it -> TypeCompiler(ActionItParent(this), type())
						else -> null
					}
				is EndToken -> null
			}
		is ActionItCompiler ->
			when (token) {
				is LiteralToken -> null
				is BeginToken ->
					when (token.begin.string) {
						parent.context.dictionary.does ->
							TypedCompiler(
								ActionItDoesParent(parent, type),
								parent.context,
								arg0<T>() of type)
						else -> null
					}
				is EndToken -> null
			}
		is ActionItDoesCompiler ->
			when (token) {
				is LiteralToken -> null
				is BeginToken -> null
				is EndToken -> parent.updateTyped { plus(action) }
			}
		is RememberCompiler ->
			when (token) {
				is LiteralToken -> null
				is BeginToken ->
					when (token.begin.string) {
						parent.context.dictionary.it -> TypeCompiler(RememberItParent(this), type())
						else -> null
					}
				is EndToken -> null
			}
		is RememberItCompiler ->
			when (token) {
				is LiteralToken -> null
				is BeginToken ->
					when (token.begin.string) {
						parent.context.dictionary.`is` -> TypedCompiler(RememberItIsParent(parent, type), parent.context, typed())
						parent.context.dictionary.does -> TypedCompiler(RememberItDoesParent(parent, type), parent.context, arg0<T>() of type)
						else -> null
					}
				is EndToken -> null
			}
		is RememberItIsCompiler ->
			when (token) {
				is LiteralToken -> null
				is BeginToken -> null
				is EndToken -> parent.plus(remember(action, false))
			}
		is RememberItDoesCompiler ->
			when (token) {
				is LiteralToken -> null
				is BeginToken -> null
				is EndToken -> parent.plus(remember(action, true))
			}
	} ?: error("$token unexpected")

fun <T> TypedParent<T>.compile(typed: Typed<T>): Compiler<T> =
	when (this) {
		is BeginTypedParent ->
			typedCompiler.updateTyped { typedCompiler.context.plus(typedCompiler.typed, begin.string fieldTo typed) }
		is MatchTypedParent ->
			matchCompiler.copy(match = Case(matchCompiler.match, typed).end())
		is ActionItDoesParent ->
			ActionItDoesCompiler(typedCompiler, itType does typed)
		is DoParent ->
			typedCompiler.updateTyped { arrow.term.invoke(typed.term) of arrow.arrow.rhs }
		is GiveParent ->
			typedCompiler.updateTyped { typed }
		is RememberItIsParent ->
			RememberItIsCompiler(typedCompiler, itType does typed)
		is RememberItDoesParent ->
			RememberItDoesCompiler(typedCompiler, itType does typed)
	}

fun <T> TypeParent<T>.compile(type: Type): Compiler<T> =
	when (this) {
		is AsParent ->
			typedCompiler.copy(typed = typedCompiler.typed.of(type))
		is BeginTypeParent ->
			typeCompiler.copy(type = typeCompiler.type.plus(begin.string lineTo type))
		is ChoiceTypeParent ->
			choiceCompiler.copy(choice = choiceCompiler.choice.plus(begin.string optionTo type))
		is ActionItParent ->
			ActionItCompiler(actionCompiler.parent, type)
		is RememberItParent ->
			RememberItCompiler(rememberCompiler.parent, type)
		is ForgetParent ->
			typedCompiler.plus(forget(type))
	}

fun <T> TypedCompiler<T>.set(typed: Typed<T>): TypedCompiler<T> =
	copy(typed = typed)

fun <T> TypedCompiler<T>.updateTyped(fn: Typed<T>.() -> Typed<T>): TypedCompiler<T> =
	set(typed.fn())

fun <T> TypedCompiler<T>.plus(line: TypedLine<T>): TypedCompiler<T> =
	updateTyped { plus(line) }

fun <T> TypeCompiler<T>.updateType(fn: Type.() -> Type): TypeCompiler<T> =
	copy(type = type.fn())

val <T> TypedCompiler<T>.retTyped: Typed<T>
	get() =
		context.ret(typed)

val <T> Compiler<T>.typed: Typed<T>
	get() =
		(this as? TypedCompiler)
			.notNullOrError("$this as TypedCompiler")
			.retTyped

fun <T> TypedCompiler<T>.plus(item: MemoryItem<T>) =
	copy(context = context.copy(memory = context.memory.plus(item)))