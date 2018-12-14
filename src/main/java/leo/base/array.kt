package leo.base

inline fun <R, V> R.fold(array: kotlin.Array<V>, fn: R.(V) -> R): R =
	array.fold(this) { folded, value ->
		folded.fn(value)
	}
