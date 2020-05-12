package leo16

import leo16.names.*

data class Function(val pattern: Pattern, val compiled: Compiled) {
	override fun toString() = asValue.toString()
}

fun Pattern.does(compiled: Compiled) = Function(this, compiled)

fun Function.apply(value: Value): Value? =
	pattern.matchOrNull(value)?.let { match ->
		compiled.invoke(match)
	}

val Function.asPatternField: Field
	get() =
		_function(pattern.asField, compiled.asField)

val Function.asValue: Value
	get() =
		pattern.asValue.plus(_does(compiled.bodyValue))

fun Dictionary.doesOrNull(value: Value): Function? =
	value.matchInfix(_does) { lhs, rhs ->
		lhs.pattern.does(compiled(rhs))
	}
