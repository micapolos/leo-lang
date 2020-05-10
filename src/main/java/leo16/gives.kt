package leo16

import leo.base.notNullIf
import leo16.names.*

data class Gives(val pattern: Pattern, val function: Function) {
	override fun toString() = asValue.toString()
}

fun Pattern.gives(function: Function) = Gives(this, function)

fun Gives.take(value: Value): Value? =
	notNullIf(value.matches(pattern)) {
		// TODO: any
		function.invoke(value.match)
	}

val Gives.asPatternField: Field
	get() =
		_taking(pattern.asField, function.asField)

val Gives.asValue: Value
	get() =
		pattern.asValue.plus(_gives(function.bodyValue))
