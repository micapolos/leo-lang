package leo16

import leo16.names.*

data class Gives(val pattern: Pattern, val function: Function) {
	override fun toString() = asValue.toString()
}

fun Pattern.gives(function: Function) = Gives(this, function)

fun Gives.apply(value: Value): Value? =
	pattern.matchOrNull(value)?.let { match ->
		function.invoke(match)
	}

val Gives.asPatternField: Field
	get() =
		_taking(pattern.asField, function.asField)

val Gives.asValue: Value
	get() =
		pattern.asValue.plus(_gives(function.bodyValue))

fun Dictionary.givesOrNull(value: Value): Gives? =
	value.matchInfix(_gives) { lhs, rhs ->
		lhs.pattern.gives(function(rhs))
	}
