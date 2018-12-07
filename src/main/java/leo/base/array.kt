package leo.base

inline fun <R, V> R.fold(array: Array<V>, fn: R.(V) -> R): R =
	array.fold(this) { folded, value ->
		folded.fn(value)
	}
