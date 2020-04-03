@file:Suppress("ReplaceToWithInfixForm")

package leo14.lambda.runtime.typed

import leo14.lambda.runtime.Value
import leo14.lambda.runtime.invoke

typealias Erase = () -> Value
typealias Arrow = Pair<Value, Value>
typealias Type = Any?

data class Typed(val type: Type, val erase: Erase)

val Typed.value: Value get() = erase()

fun typed(type: Type, eval: () -> Value) = Typed(type, eval)

fun Typed.check(type: Type): Value {
	if (this.type != type) error("${this.type} not $type")
	return erase()
}

fun Typed.checkFrom(from: Type, fn: (Value) -> Value): Typed {
	if (type !is Arrow) error("$this not a function")
	if (type.first != from) error("$this not of $from")
	return typed(type.second) {
		fn(value)
	}
}

operator fun Typed.invoke(typed: Typed): Typed =
	checkFrom(typed.type) { input ->
		input.invoke(typed.value)
	}
