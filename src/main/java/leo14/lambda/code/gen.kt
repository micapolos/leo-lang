package leo14.lambda.code

import leo14.lambda.Variable

data class Gen(val depth: Int)

fun gen(depth: Int) = Gen(depth)
val gen = gen(0)
val Gen.inc get() = Gen(depth.inc())
fun <T> Gen.inc(fn: (Gen) -> T) = fn(inc)

val Int.varCode
	get() =
		leo.base.failIfOr(this < 0) { "v$this" }

fun paramCode(gen: Gen) =
	gen.depth.varCode

fun Variable<*>.index(gen: Gen) =
	gen.depth - index - 1
