package leo.base

data class Binary(
	val bitStack: Stack<Bit>)

val Stack<Bit>.binary: Binary
	get() =
		Binary(this)

val Byte.binary: Binary
	get() =
		bitStream.stack.binary

val Short.binary: Binary
	get() =
		bitStream.stack.binary

val Int.binary: Binary
	get() =
		bitStream.stack.binary
