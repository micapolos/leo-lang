@file:Suppress("ReplaceToWithInfixForm")

package leo14.lambda.runtime.typed

import leo14.lambda.runtime.X
import leo14.lambda.runtime.invoke

typealias Eval = () -> X
typealias Arrow = Pair<X, X>

data class Typed(val type: X, val eval: Eval)

operator fun Typed.invoke(): X = eval()

fun typed(type: X, eval: () -> X) = Typed(type, eval)

fun typed(int: Int) = typed(Int::class) { int }
fun typed(string: String) = typed(String::class) { string }

fun Typed.check(type: X, fn: (X) -> Typed): Typed {
	if (this.type != type) error("${this.type} not $type")
	return fn(invoke())
}

fun Typed.checkFrom(from: X, fn: (X) -> X): Typed {
	if (type !is Arrow) error("$this not a function")
	if (type.first != from) error("$this not of $from")
	return typed(type.second) {
		fn(invoke())
	}
}

operator fun Typed.invoke(typed: Typed): Typed =
	checkFrom(typed.type) { input ->
		input.invoke(typed())
	}
