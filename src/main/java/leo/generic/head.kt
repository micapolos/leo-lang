package leo.generic

data class Head<out V>(val value: V)

operator fun <V> Head<V>.invoke() = value