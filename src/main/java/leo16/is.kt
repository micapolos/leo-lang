package leo16

import leo16.names.*

data class Is(val pattern: Pattern, val value: Value) {
	override fun toString() = asValue.toString()
}

val Is.asValue get() = pattern.asValue.plus(_is(value))
infix fun Pattern.is_(value: Value) = Is(this, value)
fun Is.apply(arg: Value): Value? = pattern.matchOrNull(arg)?.let { value }

val Value.isOrNull: Is?
	get() =
		matchInfix(_is) { lhs, rhs ->
			lhs.pattern.is_(rhs)
		}
