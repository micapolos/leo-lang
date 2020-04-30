package leo16

import leo.base.runIfNotNull

data class Closure(val scope: Scope, val value: Value)

infix fun Scope.closure(value: Value) = Closure(this, value)
val Value.closure get() = emptyScope.closure(this)
val Scope.closure get() = closure(value())

fun Closure.updateValue(fn: Value.() -> Value) = copy(value = value.fn())

val Closure.apply: Closure?
	get() =
		null
			?: applyStatic
			?: applyScope

val Closure.applyStatic: Closure?
	get() =
		scope.runIfNotNull(value.apply) { closure(it) }

val Closure.applyScope: Closure?
	get() =
		scope.apply(value)?.let { scope.closure(it) }

val Closure.begin: Closure
	get() =
		scope.closure(value())

operator fun Closure.plus(line: Line): Closure? =
	value.plus(line)?.let { copy(value = it) }
