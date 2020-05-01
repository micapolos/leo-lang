package leo16

data class Compiled(val library: Library, val value: Value)

infix fun Library.compiled(value: Value) = Compiled(this, value)
val Library.emptyCompiled get() = compiled(value())

fun Compiled.updateValue(fn: Value.() -> Value) = copy(value = value.fn())

val Compiled.begin: Compiled
	get() =
		library.begin.compiled(value())

operator fun Compiled.plus(line: Line): Compiled =
	copy(value = value.plus(line))
