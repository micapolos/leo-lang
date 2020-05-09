package leo16

import leo15.evaluatedName
import leo15.reflectName

data class Evaluated(val scope: Scope, val value: Value) {
	override fun toString() = asField.toString()
}

infix fun Scope.evaluated(value: Value) = Evaluated(this, value)
val Scope.emptyEvaluated get() = evaluated(value())

val Evaluated.asField: Field
	get() =
		evaluatedName(scope.asField, value.asField)

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
		scope.dictionary.apply(reflectName.sentenceTo(value).field.value)

val Evaluated.reflectValue: Value
	get() =
		reflectValueOrNull ?: value.print
