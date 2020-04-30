package leo16

import leo.base.runIfNotNull

data class Evaluated(val scope: Scope, val value: Value)

infix fun Scope.evaluated(value: Value) = Evaluated(this, value)

fun Evaluated.updateValue(fn: Value.() -> Value) = copy(value = value.fn())

val Evaluated.apply: Evaluated?
	get() =
		null
			?: applyStatic
			?: applyScope

val Evaluated.applyStatic: Evaluated?
	get() =
		scope.runIfNotNull(value.apply) { evaluated(it) }

val Evaluated.applyScope: Evaluated?
	get() =
		scope.apply(value)?.let { scope.evaluated(it) }

val Evaluated.begin: Evaluated
	get() =
		scope.evaluated(value())

operator fun Evaluated.plus(line: Line): Evaluated? =
	value.plus(line)?.let { copy(value = it) }
