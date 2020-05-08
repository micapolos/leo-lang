package leo16

import leo.base.notNullIf
import leo.base.runIf
import leo13.definitionName
import leo13.map
import leo15.bodyName
import leo15.givenName
import leo15.nativeName

data class Definition(val pattern: Pattern, val body: Body, val isMacro: Boolean) {
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

data class NativeBody(val apply: Value.() -> Value) : Body() {
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
				is NativeBody -> value(nativeName())
			}
		)

infix fun Pattern.definitionTo(body: Body) = Definition(this, body, isMacro = false)
infix fun Pattern.macroTo(body: Body) = Definition(this, body, isMacro = true)
val Value.body: Body get() = ValueBody(this)
val Function.body: Body get() = FunctionBody(this)
fun body(apply: Value.() -> Value): Body = NativeBody(apply)

fun Definition.apply(arg: Value): Value? =
	notNullIf(arg.matches(pattern)) {
		body.apply(arg)
	}

fun Definition.apply(evaluated: Evaluated): Evaluated? =
	apply(evaluated.value)?.let { value ->
		if (isMacro) evaluated.scope.emptyEvaluator.plus(value).evaluated
		else evaluated.set(value)
	}

fun Body.apply(arg: Value): Value =
	when (this) {
		is ValueBody -> value
		is FunctionBody -> function.invoke(arg)
		is NativeBody -> apply(givenName(arg).value)
	}

val Value.parameterDictionary: Dictionary
	get() =
		fieldStack.map { selectWord.pattern.definitionTo(value.body) }.dictionary

fun Value.gives(apply: Value.() -> Value) =
	pattern.definitionTo(
		body {
			nullIfThrowsException {
				apply(this)
			} ?: this
		})
