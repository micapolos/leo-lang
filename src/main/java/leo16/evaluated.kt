package leo16

import leo16.names.*

data class Evaluated(val scope: Scope, val value: Value) {
	override fun toString() = asField.toString()
}

infix fun Scope.evaluated(value: Value) = Evaluated(this, value)
val Scope.emptyEvaluated get() = evaluated(value())

val Value.evaluated get() = emptyScope.evaluated(this)

val Evaluated.asField: Field
	get() =
		_evaluated(scope.asField, value.asField)

fun Evaluated.updateValue(fn: Value.() -> Value) = copy(value = value.fn())

val Evaluated.begin: Evaluated
	get() =
		scope.begin.evaluated(value())

fun Evaluated.set(value: Value): Evaluated =
	copy(value = value)

val Evaluated.clearValue: Evaluated
	get() =
		set(value())

operator fun Evaluated.plus(field: Field): Evaluated =
	value.normalize(field) { set(this).plusNormalized(it) }

fun Evaluated.plusNormalized(field: Field): Evaluated =
	set(value.plus(field))

val Evaluated.reflectValueOrNull: Value?
	get() =
		scope.dictionary.apply(emptyScope.evaluated(_reflect.sentenceTo(value).field.value))?.value

val Evaluated.reflectValue: Value
	get() =
		reflectValueOrNull ?: value.printed
