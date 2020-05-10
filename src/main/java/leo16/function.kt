package leo16

import leo16.names.*

data class Function(val pattern: Pattern, val compiled: Compiled) {
	override fun toString() = asValue.toString()
}

fun Pattern.gives(compiled: Compiled) = Function(this, compiled)

fun Function.apply(value: Value): Value? =
	pattern.matchOrNull(value)?.let { match ->
		compiled.invoke(match)
	}

val Function.asPatternField: Field
	get() =
		_function(pattern.asField, compiled.asField)

val Function.asValue: Value
	get() =
		pattern.asValue.plus(_gives(compiled.bodyValue))

fun Dictionary.givesOrNull(value: Value): Function? =
	value.matchInfix(_gives) { lhs, rhs ->
		lhs.pattern.gives(compiled(rhs))
	}
