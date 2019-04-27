package leo32.base

data class At0<out V>(val value: V)

val <V> V.toAt0 get() = At0(this)
operator fun <V> At0<V>.invoke() = value
fun <V> at0(value: V) = At0(value)
