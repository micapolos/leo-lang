package leo32.base

data class SubArray<T>(
	val array: Array<T>,
	val range: IntRange)

fun <T> SubArray<T>.at(index: Int) =
	array.at(range.first + index)

fun <T> SubArray<T>.put(index: Int, value: T) =
	array.put(range.first + index, value)