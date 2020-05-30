package leo16

import leo.base.notNullIf
import leo16.names.*

data class Function(val patternValue: Value, val compiled: Compiled) {
	override fun toString() = asValue.toString()
}

fun Value.functionTo(compiled: Compiled) = Function(this, compiled)

fun Function.apply(value: Value): Value? =
	notNullIf(patternValue.matches(value)) {
		compiled.invoke(value)
	}

val Function.asPatternField: Field
	get() =
		_function(patternValue.asField, compiled.asField)

val Function.asValue: Value
	get() =
		patternValue.plus(_does(compiled.bodyValue))

fun Dictionary.doesOrNull(value: Value): Function? =
	value.matchInfix(_does) { lhs, rhs ->
		lhs.functionTo(compiled(rhs))
	}
