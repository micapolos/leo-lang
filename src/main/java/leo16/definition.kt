package leo16

import leo13.map
import leo13.reverse
import leo16.names.*

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

data class RecurseBody(val function: Function) : Body() {
	override fun toString() = super.toString()
}

data class NativeBody(val apply: Value.() -> Value) : Body() {
	override fun toString() = super.toString()
}

val Definition.asField: Field
	get() =
		_definition.invoke(pattern.asField, body.asField)

val Body.asField: Field
	get() =
		_body(
			when (this) {
				is ValueBody -> value
				is FunctionBody -> function.asField.value
				is NativeBody -> value(_native())
				is RecurseBody -> _recurse(function.asField.value).value
			}
		)

infix fun Pattern.definitionTo(body: Body) = Definition(this, body, isMacro = false)
infix fun Pattern.macroTo(body: Body) = Definition(this, body, isMacro = true)
val Value.body: Body get() = ValueBody(this)
val Function.body: Body get() = FunctionBody(this)
val Function.recurseBody: Body get() = RecurseBody(this)
fun body(apply: Value.() -> Value): Body = NativeBody(apply)

fun Definition.apply(arg: Value): Value? =
	pattern.matchOrNull(arg)?.let { match ->
		body.apply(match)
	}

fun Definition.apply(evaluated: Evaluated): Evaluated? =
	apply(evaluated.value)?.let { value ->
		if (isMacro) evaluated.scope.emptyEvaluator.plus(value).evaluated
		else evaluated.set(value)
	}

fun Body.apply(match: Match): Value =
	when (this) {
		is ValueBody -> value
		is FunctionBody -> function.invoke(match)
		is NativeBody -> apply(_given(match.value).value)
		is RecurseBody -> function.invoke(match.value.contentOrNull!!.match)
	}

val Value.parameterDictionary: Dictionary
	get() =
		fieldStack.map { parameterDefinition }.dictionary

fun Value.gives(apply: Value.() -> Value) =
	pattern.definitionTo(
		body {
			nullIfThrowsException {
				apply(this)
			} ?: this
		})

val Field.parameterDefinition: Definition
	get() =
		selectWord.pattern.definitionTo(value.body)

val Match.anyParameterDefinitionOrNull: Definition?
	get() =
		anyFieldStackOrNull?.let { fieldStack ->
			_the(fieldStack.reverse.value).parameterDefinition
		}
