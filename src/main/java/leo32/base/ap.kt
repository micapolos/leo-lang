package leo32.base

data class Ap<out T, out V>(
	val target: T,
	val value: V)

fun <T, V> T.ap(value: V) =
	Ap(this, value)
