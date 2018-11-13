package leo.base

data class The<V>(
	val value: V)

val <V> V.the
	get() =
		The(this)
