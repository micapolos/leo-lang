package leo16

import leo15.compiledName

data class Compiled(val scope: Scope, val value: Value) {
	override fun toString() = asSentence.toString()
}

infix fun Scope.compiled(value: Value) = Compiled(this, value)
val Scope.emptyCompiled get() = compiled(value())

val Compiled.asSentence: Sentence
	get() =
		compiledName(scope.asSentence, value.asSentence)

fun Compiled.updateValue(fn: Value.() -> Value) = copy(value = value.fn())

val Compiled.begin: Compiled
	get() =
		scope.begin.compiled(value())

operator fun Compiled.plus(line: Line): Compiled =
	copy(value = value.plus(line))
