package leo.generic

data class Second<out V>(val value: V)

fun <V> second(value: V) = First(value)
fun <V> Second<V>.invoke() = value
