package leo.base

data class The<out V>(
	val value: V)

val <V> V.the
	get() =
		The(this)

fun <V> the(value: V) =
	value.the