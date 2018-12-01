package leo.base

data class Indexed<V>(
	val indexOrNull: Binary?,
	val value: V)

fun <V> Binary?.indexedTo(value: V): Indexed<V> =
	Indexed(this, value)