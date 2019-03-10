package leo.binary

data class Array32<T : Any>(
	val default: T,
	val map32: Map32<T>)

val <T : Any> T.defaultArray32
	get() =
		Array32(this, nullMap32())

fun <T : Any> Array32<T>.at(index: Int) =
	map32.at(index) ?: default

fun <T : Any> Array32<T>.put(index: Int, value: T) =
	copy(map32 = map32.put(index, value))
