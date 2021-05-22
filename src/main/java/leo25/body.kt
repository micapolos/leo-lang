package leo25

import leo14.Script

sealed class Body
data class BlockBody(val block: Block) : Body()
data class FnBody(val fn: (Value) -> Value) : Body()

fun body(block: Block): Body = BlockBody(block)
fun body(fn: (Value) -> Value): Body = FnBody(fn)
fun body(script: Script): Body = body(block(script))

fun unsafeBody(fn: Value.() -> Value): Body = body { it.fn() }
