package leo14.typed.compiler

import leo13.reverse
import leo14.*
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
					TODO()
				is BeginToken ->
					when (token.begin.string) {
						"of" ->
							TypeCompiler(OfParent(this), type())
						"match" ->
							MatchCompiler(
								this,
								Match(
									typed.term,
									typed.onlyLine.choice.choice.optionStack.reverse,
									null))
						else ->
							TypedCompiler(BeginTypedParent(this, token.begin), emptyTyped())
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
					parent?.copy(type = parent.type.plus(line(choice)))
			}
		is MatchCompiler ->
			when (token) {
				is LiteralToken ->
					null
				is BeginToken -> {
					val line = match.begin(token.begin.string)
					TypedCompiler(
						MatchTypedParent(MatchCompiler(parent, line.match)),
						line.typed)
				}
				is EndToken ->
					parent?.copy(typed = match.end())
			}
	} ?: error("$token")

fun <T> TypedParent<T>.compile(typed: Typed<T>): Compiler<T> =
	when (this) {
		is BeginTypedParent ->
			typedCompiler.copy(typed = typedCompiler.typed.plus(line(begin.string fieldTo typed)))
		is MatchTypedParent ->
			matchCompiler.copy(match = Case(matchCompiler.match, typed).end())
	}

fun <T> TypeParent<T>.compile(type: Type): Compiler<T> =
	when (this) {
		is OfParent ->
			typedCompiler.copy(typed = typedCompiler.typed.castTypedTo(type))
		is BeginTypeParent ->
			typeCompiler.copy(type = typeCompiler.type.plus(begin.string lineTo type))
		is ChoiceTypeParent ->
			choiceCompiler.copy(choice = choiceCompiler.choice.plus(begin.string optionTo type))
	}
