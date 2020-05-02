package leo16

import leo.base.notNullIf
import leo15.bindingName
import leo15.bodyName
import leo15.givenName

data class Binding(val pattern: Pattern, val body: Body) {
	override fun toString() = asField.toString()
}

sealed class Body {
	override fun toString() = asField.toString()
}

data class ValueBody(val value: Value) : Body() {
	override fun toString() = super.toString()
}

data class FunctionBody(val function: Function) : Body() {
	override fun toString() = super.toString()
}

val Binding.asField: Field
	get() =
		bindingName.invoke(pattern.asField, body.asField)

val Body.asField: Field
	get() =
		bodyName(
			when (this) {
				is ValueBody -> value
				is FunctionBody -> function.asField.value
			}
		)

infix fun Pattern.bindingTo(body: Body) = Binding(this, body)
val Value.body: Body get() = ValueBody(this)
val Function.body: Body get() = FunctionBody(this)

fun Binding.apply(arg: Value): Value? =
	notNullIf(arg.matches(pattern)) {
		body.apply(arg)
	}

fun Body.apply(arg: Value): Value =
	when (this) {
		is ValueBody -> value
		is FunctionBody -> function.invoke(arg)
	}

val Value.givenBinding: Binding
	get() =
		givenName.pattern bindingTo value(givenName.invoke(this)).body

