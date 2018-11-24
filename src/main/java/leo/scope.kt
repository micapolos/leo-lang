package leo

import leo.base.Bit
import leo.base.Stream
import leo.base.string

data class Scope(
	val function: Function,
	val termOrNull: Term<Nothing>?) {
	override fun toString() = reflect.string
}

val emptyScope =
	Scope(emptyFunction, null)

fun Scope.push(word: Word): Scope? =
	if (termOrNull == null) copy(termOrNull = word.term)
	else null

fun Scope.push(field: Field<Nothing>): Scope? =
	termOrNull?.push(field)?.let { pushedTerm ->
		copy(termOrNull = pushedTerm)
	}

val Scope.evaluate: Scope
	get() = parseRule
		?: evaluateSelect
			.invokeFunction

val Scope.evaluateSelect: Scope
	get() =
		copy(termOrNull = termOrNull?.evaluateSelect)

val Scope.parseRule: Scope?
	get() =
		termOrNull?.parseRule(function)?.let(this::push)

val Scope.invokeFunction: Scope
	get() =
		copy(termOrNull = termOrNull?.let { term ->
			function.invoke(term) { term.invokeFallback }
		})

fun Scope.push(rule: Rule): Scope? =
	function.define(rule)?.let { newFunction ->
		copy(function = newFunction, termOrNull = null)
	}

// === reflect ===

val Scope.reflect: Field<Nothing>
	get() =
		scopeWord fieldTo term(
			function.reflect,
			termOrNull.orNullReflect(termWord, Term<Nothing>::reflect))

// === bit stream

fun Scope.bitStreamOrNull(asFieldsTerm: Boolean): Stream<Bit>? =
	termOrNull?.let { term ->
		when {
			asFieldsTerm && term !is FieldsTerm -> term.itTerm
			else -> term
		}.bitStream
	}
