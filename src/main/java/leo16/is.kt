package leo16

import leo16.names.*

data class Is(val pattern: Pattern, val value: Value) {
	override fun toString() = asValue.toString()
}

val Is.asValue get() = pattern.asValue.plus(_is(value))
infix fun Pattern.is_(value: Value) = Is(this, value)
fun Is.invoke(arg: Value) = pattern.matchOrNull(arg)?.let { value }