package leo32.base

data class At1<out V>(val value: V)

val <V> V.toAt1 get() = At1(this)
operator fun <V> At1<V>.invoke() = value
fun <V> at1(value: V) = At1(value)
