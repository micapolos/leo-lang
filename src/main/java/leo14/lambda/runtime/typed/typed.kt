@file:Suppress("ReplaceToWithInfixForm")

package leo14.lambda.runtime.typed

import leo14.lambda.runtime.Fn
import leo14.lambda.runtime.Value
import leo14.lambda.runtime.invoke as untypedInvoke

typealias Erase = () -> Value
typealias Type = Any?

data class Arrow(val from: Value, val to: Value) {
	override fun toString() = "$from.to($to)"
}

data class Typed(val type: Type, val erase: Erase) {
	override fun toString() = "$value.of($type)"
}

data class Or(val lhs: Value, val rhs: Value) {
	override fun toString() = "$lhs.or($rhs)"
}

val Typed.value: Value get() = erase()
fun typed(type: Type, erase: Erase) = Typed(type, erase)
infix fun Value.to(to: Value) = Arrow(this, to)
infix fun Value.or(value: Value) = Or(this, value)

fun Typed.check(type: Type): Value {
	if (this.type != type) error("${this.type} not $type")
	return value
}

fun Typed.checkFrom(from: Type, fn: Fn): Typed {
	if (type !is Arrow) error("not a function: $this")
	if (type.from != from) error("expected: ${type.from}, got: $from")
	return typed(type.to) {
		fn(value)
	}
}

fun fn(arrow: Arrow, fn: Fn): Typed =
	typed(arrow) { fn }

operator fun Typed.invoke(typed: Typed): Typed =
	checkFrom(typed.type) { input ->
		input.untypedInvoke(typed.value)
	}
