package leo.generic

data class First<out V>(val value: V)

fun <V> first(value: V) = First(value)
operator fun <V> First<V>.invoke() = value
