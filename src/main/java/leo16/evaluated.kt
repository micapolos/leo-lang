package leo16

import leo16.names.*

data class Evaluated(val scope: Scope, val value: Value) {
	override fun toString() = asField.toString()
}

infix fun Scope.evaluated(value: Value) = Evaluated(this, value)
val Scope.emptyEvaluated get() = evaluated(emptyValue)

val Value.evaluated get() = emptyScope.evaluated(this)
val emptyEvaluated get() = emptyScope.emptyEvaluated

val Evaluated.asField: Sentence
	get() =
		_evaluated(scope.asField, value.asSentence)

fun Evaluated.updateValue(fn: Value.() -> Value) = copy(value = value.fn())

val Evaluated.begin: Evaluated
	get() =
		scope.beginEvaluated

fun Evaluated.set(value: Value): Evaluated =
	copy(value = value)

val Evaluated.clearValue: Evaluated
	get() =
		set(value())

operator fun Evaluated.plus(field: Sentence): Evaluated =
	value.normalize(field) { set(this).plusNormalized(it) }

fun Evaluated.plusNormalized(field: Sentence): Evaluated =
	set(value.plus(field))

val Evaluated.reflectValue: Value
	get() =
		scope.dictionary.applyReflect(value)