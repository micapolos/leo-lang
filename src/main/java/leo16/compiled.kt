package leo16

import leo16.names.*

data class Compiled(val dictionary: Dictionary, val bodyValue: Value) {
	override fun toString() = asField.toString()
}

data class CompiledSentence(val word: String, val compiled: Compiled)

infix fun Dictionary.compiled(value: Value) = Compiled(this, value)
fun String.sentenceTo(compiled: Compiled) = CompiledSentence(this, compiled)
val Value.compiled get() = emptyDictionary.compiled(this)

val Compiled.isEmpty get() = bodyValue.isEmpty

inline operator fun Compiled.invoke(match: PatternMatch): Value =
	dictionary.plus(repeat.definition).plus(match).evaluate(bodyValue)

inline operator fun Compiled.invoke(value: Value): Value =
	dictionary.plus(repeat.definition).bind(value).evaluate(bodyValue)

val Compiled.asField: Field
	get() =
		_function(/*dictionary.asField, */bodyValue.asField)

val Compiled.evaluate: Value
	get() =
		dictionary.evaluate(bodyValue)