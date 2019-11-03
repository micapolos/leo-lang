package leo14.lambda.code

data class Gen(val depth: Int)

fun gen(depth: Int) = Gen(depth)
val gen = gen(0)
val Gen.inc get() = Gen(depth.inc())
fun <T> Gen.inc(fn: (Gen) -> T) = fn(inc)
