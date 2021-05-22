package leo25

import leo14.*

sealed class Body
data class BlockBody(val block: Block) : Body()
data class FnBody(val fn: Dictionary.(Value) -> Value) : Body()

fun body(block: Block): Body = BlockBody(block)
fun body(fn: Dictionary.(Value) -> Value): Body = FnBody(fn)
fun body(script: Script): Body = body(block(script))

fun unsafeBody(fn: Value.() -> Value): Body = body { it.fn() }

fun Body.apply(dictionary: Dictionary, given: Value): Value =
	when (this) {
		is FnBody -> dictionary.fn(given)
		is BlockBody -> dictionary.apply(block, given)
	}
