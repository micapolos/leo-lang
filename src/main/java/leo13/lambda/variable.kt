package leo13.lambda

data class Variable<out T>(val index: Int)

fun <T> variable(index: Int) = Variable<T>(index)
fun <T> variable() = variable<T>(0)
val <T> Variable<T>.inc get() = variable<T>(index.inc())
