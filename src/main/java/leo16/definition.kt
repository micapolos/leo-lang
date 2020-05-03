package leo16

import leo.base.notNullIf
import leo13.definitionName
import leo15.bodyName
import leo15.givenName

data class Definition(val pattern: Pattern, val body: Body) {
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

val Definition.asField: Field
	get() =
		definitionName.invoke(pattern.asField, body.asField)

val Body.asField: Field
	get() =
		bodyName(
			when (this) {
				is ValueBody -> value
				is FunctionBody -> function.asField.value
			}
		)

infix fun Pattern.definitionTo(body: Body) = Definition(this, body)
val Value.body: Body get() = ValueBody(this)
val Function.body: Body get() = FunctionBody(this)

fun Definition.apply(arg: Value): Value? =
	notNullIf(arg.matches(pattern)) {
		body.apply(arg)
	}

fun Body.apply(arg: Value): Value =
	when (this) {
		is ValueBody -> value
		is FunctionBody -> function.invoke(arg)
	}

val Value.givenDefinition: Definition
	get() =
		givenName.pattern definitionTo value(givenName.invoke(this)).body
