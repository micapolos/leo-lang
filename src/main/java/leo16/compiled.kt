package leo16

import leo15.compiledName

data class Compiled(val library: Library, val value: Value) {
	override fun toString() = asSentence.toString()
}

infix fun Library.compiled(value: Value) = Compiled(this, value)
val Library.emptyCompiled get() = compiled(value())

val Compiled.asSentence: Sentence
	get() =
		compiledName(library.asSentence, value.asSentence)

fun Compiled.updateValue(fn: Value.() -> Value) = copy(value = value.fn())

val Compiled.begin: Compiled
	get() =
		library.begin.compiled(value())

operator fun Compiled.plus(line: Line): Compiled =
	copy(value = value.plus(line))
