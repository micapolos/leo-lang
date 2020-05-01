package leo16

import leo.base.notNullIf
import leo15.bindingName
import leo15.bodyName
import leo15.givenName
import leo15.matchingName

data class Binding(val pattern: Pattern, val body: Body) {
	override fun toString() = asSentence.toString()
}

sealed class Body {
	override fun toString() = asSentence.toString()
}

data class ValueBody(val value: Value) : Body() {
	override fun toString() = super.toString()
}

data class FunctionBody(val function: Function) : Body() {
	override fun toString() = super.toString()
}

val Binding.asSentence: Sentence
	get() =
		bindingName.invoke(pattern.asSentence, body.asSentence)

val Body.asSentence: Sentence
	get() =
		bodyName(
			when (this) {
				is ValueBody -> value.asSentence
				is FunctionBody -> function.asSentence
			}
		)

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

val Value.matchingBinding: Binding
	get() =
		matchingName.pattern bindingTo value(matchingName.invoke(this)).body

val Value.givenBinding: Binding
	get() =
		givenName.pattern bindingTo value(givenName.invoke(this)).body