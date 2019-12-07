package leo14.typed.compiler

import leo13.EmptyStack
import leo13.LinkStack
import leo13.Stack
import leo13.reverse
import leo14.ScriptLine
import leo14.lambda.Term
import leo14.lambda.arg0
import leo14.lambda.invoke
import leo14.lambda.scriptLine
import leo14.lineTo
import leo14.script
import leo14.typed.*

data class Match<T>(
	val term: Term<T>,
	val optionStack: Stack<Option>,
	val typeOrNull: Type?)

data class Case<T>(
	val match: Match<T>,
	val typed: Typed<T>)

fun <T> Match<T>.beginCase(string: String): Case<T> =
	when (optionStack) {
		is EmptyStack -> error("match exhausted when adding: $string")
		is LinkStack -> {
			val (plusOptionStack, case) = optionStack.link
			if (case.string != string)
				error("case name mismatch, was: ${string}, should be: ${case.string}")
			Case(Match(term, plusOptionStack, typeOrNull), arg0<T>() of case.rhs)
		}
	}

fun <T> Case<T>.end(): Match<T> {
	if (match.typeOrNull != null && match.typeOrNull != typed.type)
		error("case type mismatch, was: ${typed.type}, should be: ${match.typeOrNull}")
	val plusTerm = match.term.invoke(typed.term)
	val plusType = match.typeOrNull ?: typed.type
	return Match(plusTerm, match.optionStack, plusType)
}

fun <T> Case<T>.update(fn: Typed<T>.() -> Typed<T>) =
	copy(typed = typed.fn())

fun <T> Match<T>.end(): Typed<T> =
	if (typeOrNull == null) error("impossible match")
	else when (optionStack) {
		is EmptyStack -> term of typeOrNull
		is LinkStack -> error("non exhaustive match, expected: ${optionStack.link.value}")
	}

fun <T> Typed<T>.beginMatch(): Match<T> =
	type.onlyLine.choice.let { choice ->
		Match(term, choice.optionStack.reverse, null)
	}

fun <T> Match<T>.reflectScriptLine(nativeFn: T.() -> ScriptLine) =
		"match" lineTo script(
			term.scriptLine(nativeFn),
			typeOrNull?.scriptLine ?: "type" lineTo script("null"))

fun <T> Case<T>.reflectScriptLine(nativeFn: T.() -> ScriptLine) =
	"case" lineTo script(typed.reflectScriptLine(nativeFn))