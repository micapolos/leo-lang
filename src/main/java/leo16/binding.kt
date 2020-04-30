package leo16

import leo.base.notNullIf

data class Binding(val pattern: Pattern, val body: Body)

sealed class Body
data class ValueBody(val value: Value) : Body()
data class FunctionBody(val function: Function) : Body()

infix fun Pattern.bindingTo(body: Body) = Binding(this, body)
val Value.body: Body get() = ValueBody(this)
val Function.body: Body get() = FunctionBody(this)

fun Binding.apply(value: Value): Value? =
	notNullIf(value.matches(pattern)) {
		body.apply(value)
	}

fun Body.apply(arg: Value): Value =
	when (this) {
		is ValueBody -> value
		is FunctionBody -> function.invoke(arg)
	}
