package leo

import leo.base.Bit
import leo.base.Stream

data class Scope(
	val function: Function,
	val termOrNull: Term<Nothing>?) {
	//override fun toString() = reflect.string
}

val emptyScope =
	Scope(emptyFunction, null)

fun Scope.push(word: Word) =
	copy(termOrNull = termOrNull.push(word))

fun Scope.push(field: Field<Nothing>) =
	copy(termOrNull = termOrNull.push(field))

val Scope.evaluate: Scope
	get() = parseRule
		?: evaluateSelect
			.invokeFunction

val Scope.evaluateSelect: Scope
	get() =
		copy(termOrNull = termOrNull?.select)

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

//val Scope.reflect: Field<Nothing>
//	get() =
//		scopeWord fieldTo term(
//			function.reflect,
//			termWord fieldTo termOrNull)

// === bit stream

val Scope.bitStreamOrNull: Stream<Bit>?
	get() =
		termOrNull?.bitStream
