package leo16

import leo15.functionName

data class Function(val library: Library, val bodyValue: Value) {
	override fun toString() = asField.toString()
}

infix fun Library.function(value: Value) = Function(this, value)

operator fun Function.invoke(value: Value): Value =
	library.plus(value.givenDefinition).evaluate(bodyValue)!!

val Function.asField: Field
	get() =
		functionName(library.asField, bodyValue.asField)
