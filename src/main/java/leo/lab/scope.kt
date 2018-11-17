package leo.lab

import leo.Word
import leo.base.Bit
import leo.base.Stream
import leo.base.string
import leo.scopeWord
import leo.termWord

data class Scope(
	val function: Function,
	val termOrNull: Term<Nothing>?) {
	override fun toString() = reflect.string
}

val emptyScope =
	Scope(identityFunction, null)

fun Scope.push(word: Word) =
	copy(termOrNull = termOrNull.push(word))

fun Scope.push(field: Field<Nothing>) =
	copy(termOrNull = termOrNull.push(field))

val Scope.evaluate: Scope
	get() = evaluateSelect.run {
		null
			?: parseRule
			?: invokeFunction
	}

val Scope.evaluateSelect: Scope
	get() =
		copy(termOrNull = termOrNull?.select)

val Scope.parseRule: Scope?
	get() =
		termOrNull?.parseRule(function)?.let(this::push)

val Scope.invokeFunction: Scope
	get() =
		copy(termOrNull = termOrNull?.let { term ->
			function.invoke(term)
		})

fun Scope.push(rule: Rule): Scope =
	copy(function = function.push(rule), termOrNull = null)

// === reflect ===

val Scope.reflect: Field<Nothing>
	get() =
		scopeWord fieldTo term(
			function.reflect,
			termWord fieldTo termOrNull)

// === bit stream

val Scope.bitStreamOrNull: Stream<Bit>?
	get() =
		termOrNull?.bitStream