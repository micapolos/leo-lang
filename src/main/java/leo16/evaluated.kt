package leo16

import leo15.compiledName

data class Evaluated(val scope: Scope, val value: Value) {
	override fun toString() = asSentence.toString()
}

infix fun Scope.evaluated(value: Value) = Evaluated(this, value)
val Scope.emptyEvaluated get() = evaluated(value())
val emptyEvaluated get() = emptyScope.emptyEvaluated

val Evaluated.asSentence: Field
	get() =
		compiledName(scope.asField, value.asField)

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
	set(value.plus(field))
