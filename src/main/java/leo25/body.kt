package leo25

import leo14.Script

sealed class Body
data class BlockBody(val block: Block) : Body()
data class FnBody(val fn: (Resolver) -> Value) : Body()

fun body(block: Block): Body = BlockBody(block)
fun body(fn: Resolver.() -> Value): Body = FnBody(fn)
fun body(script: Script): Body = body(block(script))
