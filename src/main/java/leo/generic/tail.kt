package leo.generic

data class Tail<out V>(val value: V)

operator fun <V> Tail<V>.invoke() = value
