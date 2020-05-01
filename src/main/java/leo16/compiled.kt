package leo16

data class Compiled(val scope: Scope, val value: Value)

infix fun Scope.compiled(value: Value) = Compiled(this, value)

fun Compiled.updateValue(fn: Value.() -> Value) = copy(value = value.fn())

val Compiled.begin: Compiled
	get() =
		scope.compiled(value())

operator fun Compiled.plus(line: Line): Compiled =
	copy(value = value.plus(line))
