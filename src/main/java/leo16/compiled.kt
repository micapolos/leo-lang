package leo16

import leo16.names.*

data class Compiled(val dictionary: Dictionary, val bodyValue: Value) {
	override fun toString() = asField.toString()
}

data class CompiledSentence(val word: String, val compiled: Compiled)

infix fun Dictionary.compiled(value: Value) = Compiled(this, value)
fun String.sentenceTo(compiled: Compiled) = CompiledSentence(this, compiled)

val Compiled.isEmpty get() = bodyValue.isEmpty

operator fun Compiled.invoke(match: PatternMatch): Value =
	null
		?: invokeRepeatingOrNull(match)
		?: invokeRecursingOrNull(match)
		?: invokeOnce(match)

fun Compiled.invokeRepeatingOrNull(match: PatternMatch): Value? =
	bodyValue.matchPrefix(_repeating) { repeatingValue ->
		// TODO: We are login match after one repetition.
		var repeated = match.value
		while (true) {
			val evaluated = dictionary
				.plus(repeated.parameterDictionary)
				.evaluate(repeatingValue)!!
			val repeatOrNull = evaluated.rhsOrNull(_repeat)
			if (repeatOrNull == null) {
				repeated = evaluated
				break
			} else {
				repeated = repeatOrNull
			}
		}
		repeated
	}

fun Compiled.invokeRecursingOrNull(match: PatternMatch): Value? =
	bodyValue.matchPrefix(_recursing) { recursingValue ->
		// TODO: We are login match after one repetition.
		dictionary
			.plus(match)
			//.plus(_recurse(_any()).value.pattern.definitionTo(recurseBody))
			.evaluate(recursingValue)!!
	}

fun Compiled.invokeOnce(match: PatternMatch): Value =
	dictionary.plus(match).evaluate(bodyValue)!!


val Compiled.asField: Field
	get() =
		_function(/*dictionary.asField, */bodyValue.asField)
