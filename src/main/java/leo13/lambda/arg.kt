package leo13.lambda

data class Arg<out T>(val index: Int)

fun <T> arg(index: Int) = Arg<T>(index)
fun <T> arg() = arg<T>(0)
val <T> Arg<T>.inc get() = arg<T>(index.inc())
