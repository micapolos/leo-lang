package leo32.base

data class Other<out V>(
	val value: V)

fun <V> other(value: V) = Other(value)