package leo.base

data class Binary(
	val bitStack: Stack<Bit>) {
	override fun toString() = appendableString { it.append(this) }
}

fun Appendable.appendBit(bitStream: Stream<Bit>): Appendable =
	append(bitStream.first)
		.ifNotNull(bitStream.nextOrNull, Appendable::appendBit)

fun Appendable.append(binary: Binary): Appendable =
	append("0b").appendBit(binary.bitStack.reverse.stream)

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
