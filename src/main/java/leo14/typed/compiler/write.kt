package leo14.typed.compiler

import leo.base.ifOrNull
import leo13.EmptyStack
import leo13.LinkStack
import leo13.isEmpty
import leo13.reverse
import leo14.*
import leo14.lambda.arg0
import leo14.lambda.fn
import leo14.lambda.invoke
import leo14.typed.*

// === Script writing

fun <T> Compiler<T>.write(script: Script): Compiler<T> =
	when (script) {
		is UnitScript -> this
		is LinkScript -> write(script.link)
	}

fun <T> Compiler<T>.write(link: ScriptLink): Compiler<T> =
	write(link.lhs).write(link.line)

fun <T> Compiler<T>.write(line: ScriptLine): Compiler<T> =
	when (line) {
		is LiteralScriptLine -> write(token(line.literal))
		is FieldScriptLine -> write(line.field)
	}

fun <T> Compiler<T>.write(field: ScriptField): Compiler<T> =
	write(token(begin(field.string))).write(field.rhs).write(token(end))

// === Compiling

fun <T> Compiler<T>.write(token: Token): Compiler<T> =
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
							MatchCompiler(this, typed.term, typed.onlyLine.choice.choice.caseStack.reverse, null)
						else ->
							TypedCompiler(BeginTypedParent(this, token.begin), emptyTyped())
					}
				is EndToken ->
					parent?.write(typed)
			}
		is TypeCompiler ->
			when (token) {
				is LiteralToken ->
					null
				is BeginToken ->
					when (token.begin.string) {
						"choice" -> ChoiceCompiler(this, choice())
						else -> TypeCompiler(BeginTypeParent(this, token.begin), type())
					}
				is EndToken ->
					parent?.write(type)
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
				is BeginToken ->
					when (remainingCaseStack) {
						is EmptyStack ->
							error("choice already exhausted")
						is LinkStack ->
							if (remainingCaseStack.link.value.string != token.begin.string)
								error("wrong case")
							else
								TypedCompiler(
									MatchTypedParent(copy(remainingCaseStack = remainingCaseStack.link.stack)),
									arg0<T>() of remainingCaseStack.link.value.rhs)
					}
				is EndToken ->
					ifOrNull(typeOrNull != null && remainingCaseStack.isEmpty) {
						parent
					}
			}
	} ?: error("$token")

fun <T> TypedParent<T>.write(typed: Typed<T>): Compiler<T> =
	when (this) {
		is BeginTypedParent ->
			typedCompiler.copy(typed = typedCompiler.typed.plus(line(begin.string fieldTo typed)))
		is MatchTypedParent ->
			matchCompiler.copy(
				term = matchCompiler.term.invoke(fn(typed.term)),
				typeOrNull = typed.type) // TODO: Check type
	}

fun <T> TypeParent<T>.write(type: Type): Compiler<T> =
	when (this) {
		is OfParent ->
			typedCompiler.copy(typed = typedCompiler.typed.castTypedTo(type))
		is BeginTypeParent ->
			typeCompiler.copy(type = typeCompiler.type.plus(begin.string lineTo type))
		is ChoiceTypeParent ->
			choiceCompiler.copy(choice = choiceCompiler.choice.plus(begin.string caseTo type))
	}
