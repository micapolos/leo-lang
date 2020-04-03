@file:Suppress("ReplaceToWithInfixForm")

package leo14.lambda.runtime.typed

import leo14.lambda.runtime.X
import leo14.lambda.runtime.invoke

typealias Untyped = X
typealias Erase = () -> Untyped
typealias Arrow = Pair<Untyped, Untyped>
typealias Type = Any?

data class Typed(val type: Type, val erase: Erase)

val Typed.untyped: Untyped get() = erase()

fun typed(type: Type, eval: () -> X) = Typed(type, eval)

fun Typed.check(type: Type): X {
	if (this.type != type) error("${this.type} not $type")
	return erase()
}

fun Typed.checkFrom(from: Type, fn: (X) -> X): Typed {
	if (type !is Arrow) error("$this not a function")
	if (type.first != from) error("$this not of $from")
	return typed(type.second) {
		fn(untyped)
	}
}

operator fun Typed.invoke(typed: Typed): Typed =
	checkFrom(typed.type) { input ->
		input.invoke(typed.untyped)
	}
