package leo25

import leo14.*

sealed class Body
data class BlockBody(val block: Block) : Body()
data class FnBody(val fn: Context.(Value) -> Value) : Body()

fun body(block: Block): Body = BlockBody(block)
fun body(fn: Context.(Value) -> Value): Body = FnBody(fn)
fun body(script: Script): Body = body(block(script))

fun unsafeBody(fn: Value.() -> Value): Body = body { it.fn() }

fun Body.apply(context: Context, given: Value): Value =
	when (this) {
		is FnBody -> context.fn(given)
		is BlockBody -> context.apply(block, given)
	}
