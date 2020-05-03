package leo16

import leo15.functionName

data class Function(val dictionary: Dictionary, val bodyValue: Value) {
	override fun toString() = asField.toString()
}

infix fun Dictionary.function(value: Value) = Function(this, value)

operator fun Function.invoke(value: Value): Value =
	dictionary.plus(value.givenDefinition).evaluate(bodyValue)!!

val Function.asField: Field
	get() =
		functionName(dictionary.asField, bodyValue.asField)
