package leo14.typed.compiler

import leo13.reverse
import leo14.*
import leo14.lambda.arg0
import leo14.lambda.fn
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
					copy(typed = typed.plus(term(token.literal.lit()) of nativeLine))
				is BeginToken ->
					when (token.begin.string) {
						"delete" ->
							DeleteCompiler(this)
						"as" ->
							TypeCompiler(AsParent(this), type())
						"match" ->
							MatchCompiler(
								this,
								Match(
									typed.term,
									typed.onlyLine.choice.choice.optionStack.reverse,
									null))
						"any" ->
							TypeCompiler(AnyTypeParent(this), type())
						"apply" ->
							typed.onlyLine.arrow.let { arrow ->
								TypedCompiler(ApplyParent(this, arrow), typed(), lit)
							}
						else ->
							TypedCompiler(BeginTypedParent(this, token.begin), typed(), lit)
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
					parent.copy(type = parent.type.plus(line(choice)))
			}
		is MatchCompiler ->
			when (token) {
				is LiteralToken ->
					null
				is BeginToken -> {
					val line = match.beginCase(token.begin.string)
					TypedCompiler(
						MatchTypedParent(MatchCompiler(parent, line.match)),
						line.typed,
						parent.lit)
				}
				is EndToken ->
					parent.copy(typed = match.end())
			}
		is DeleteCompiler ->
			when (token) {
				is LiteralToken -> TODO()
				is BeginToken -> TODO()
				is EndToken -> parent.copy(typed = typed())
			}
		is AnyCompiler ->
			when (token) {
				is LiteralToken ->
					TODO()
				is BeginToken ->
					when (token.begin.string) {
						"does" ->
							TypedCompiler(
								AnyDoesParent(parent, type),
								arg0<T>() of type,
								parent.lit)
						else ->
							TODO()
					}
				is EndToken ->
					TODO()
			}
	} ?: error("$token")

fun <T> TypedParent<T>.compile(typed: Typed<T>): Compiler<T> =
	when (this) {
		is BeginTypedParent ->
			typedCompiler.copy(typed = typedCompiler.typed.eval(begin.string fieldTo typed))
		is MatchTypedParent ->
			matchCompiler.copy(match = Case(matchCompiler.match, typed).end())
		is AnyDoesParent ->
			typedCompiler.copy(typed = typedCompiler.typed.plus(fn(typed.term) of line(paramType arrowTo typed.type)))
		is ApplyParent ->
			typedCompiler.copy(typed = arrow.term.invoke(typed.term) of arrow.arrow.rhs)
	}

fun <T> TypeParent<T>.compile(type: Type): Compiler<T> =
	when (this) {
		is AsParent ->
			typedCompiler.copy(typed = typedCompiler.typed.of(type))
		is BeginTypeParent ->
			typeCompiler.copy(type = typeCompiler.type.plus(begin.string lineTo type))
		is ChoiceTypeParent ->
			choiceCompiler.copy(choice = choiceCompiler.choice.plus(begin.string optionTo type))
		is AnyTypeParent ->
			AnyCompiler(typedCompiler, type)
	}
