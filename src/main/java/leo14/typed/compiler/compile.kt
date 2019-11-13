package leo14.typed.compiler

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
						"give" ->
							TypedCompiler(GiveParent(this), context, typed())
						"as" ->
							TypeCompiler(AsParent(this), type())
						"match" ->
							MatchCompiler(
								this,
								Match(
									typed.term,
									typed.onlyLine.choice.choice.optionStack.reverse,
									null))
						"action" ->
							ActionCompiler(this)
						"remember" ->
							RememberCompiler(this)
						"do" ->
							typed.onlyLine.arrow.let { arrow ->
								TypedCompiler(DoParent(this, arrow), context, typed())
							}
						else ->
							TypedCompiler(BeginTypedParent(this, token.begin), context, typed())
					}
				is EndToken ->
					parent?.compile(typed)
			}
		is TypeCompiler ->
			when (token) {
				is LiteralToken ->
					null
				is BeginToken ->
					when (token.begin.string) {
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
						"it" -> TypeCompiler(ActionItParent(this), type())
						else -> null
					}
				is EndToken -> null
			}
		is ActionItCompiler ->
			when (token) {
				is LiteralToken -> null
				is BeginToken ->
					when (token.begin.string) {
						"does" ->
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
				is BeginToken -> null
				is EndToken ->
					parent.typed.onlyLine.arrow.let { arrow ->
						TypedCompiler(
							parent.parent,
							parent.context.remember(arrow.arrow),
							parent.typed) // TODO: the following code must be enclosed inside a function!!!
					}
			}
	} ?: error("$token unexpected")

fun <T> TypedParent<T>.compile(typed: Typed<T>): Compiler<T> =
	when (this) {
		is BeginTypedParent ->
			typedCompiler.updateTyped { eval(begin.string fieldTo typed) }
		is MatchTypedParent ->
			matchCompiler.copy(match = Case(matchCompiler.match, typed).end())
		is ActionItDoesParent ->
			ActionItDoesCompiler(typedCompiler, paramType ret typed)
		is DoParent ->
			typedCompiler.updateTyped { arrow.term.invoke(typed.term) of arrow.arrow.rhs }
		is GiveParent ->
			typedCompiler.updateTyped { typed }
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
	}

fun <T> TypedCompiler<T>.set(typed: Typed<T>): TypedCompiler<T> =
	copy(typed = typed)

fun <T> TypedCompiler<T>.updateTyped(fn: Typed<T>.() -> Typed<T>): TypedCompiler<T> =
	set(typed.fn())

fun <T> TypedCompiler<T>.plus(line: TypedLine<T>): TypedCompiler<T> =
	updateTyped { plus(line) }

fun <T> TypeCompiler<T>.updateType(fn: Type.() -> Type): TypeCompiler<T> =
	copy(type = type.fn())


