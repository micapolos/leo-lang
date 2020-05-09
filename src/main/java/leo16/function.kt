package leo16

import leo15.functionName
import leo16.names.*

data class Function(val dictionary: Dictionary, val bodyValue: Value) {
	override fun toString() = asField.toString()
}

infix fun Dictionary.function(value: Value) = Function(this, value)

operator fun Function.invoke(value: Value): Value =
	null
		?: invokeRepeatingOrNull(value)
		?: invokeRecursingOrNull(value)
		?: invokeOnce(value)

fun Function.invokeRepeatingOrNull(value: Value): Value? =
	bodyValue.matchPrefix(_repeating) { repeatingValue ->
		var repeated = value
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

fun Function.invokeRecursingOrNull(value: Value): Value? =
	null // TODO()

fun Function.invokeOnce(value: Value): Value =
	dictionary.plus(value.parameterDictionary).evaluate(bodyValue)!!


val Function.asField: Field
	get() =
		functionName(/*dictionary.asField, */bodyValue.asField)
