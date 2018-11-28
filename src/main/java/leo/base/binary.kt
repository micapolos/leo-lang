package leo.base

data class Binary(
	val bitStack: Stack<Bit>) {
	override fun toString() = appendableString { it.append(this) }
}

fun Appendable.appendBit(bitStream: Stream<Bit>): Appendable =
	append(bitStream.first)
		.ifNotNull(bitStream.nextOrNull, Appendable::appendBit)

fun Appendable.append(binary: Binary): Appendable =
	append("0b").appendBit(binary.bitStream)

val Binary.bitStream: Stream<Bit>
	get() =
		bitStack.reverse.stream

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

fun Int.binaryOrNullWithSize(size: Int): Binary? =
	(this to nullOf<Stack<Bit>>()).iterate(size) {
		first.shr(1) to second.push(first.lastBit)
	}.second?.reverse?.binary

val Binary.clampedInt: Int
	get() =
		0.fold(bitStream) { bit ->
			toInt().shl(1).or(bit.int)
		}

val Binary.clampedShort: Short
	get() =
		clampedInt.toShort()

val Binary.clampedByte: Byte
	get() =
		clampedShort.toByte()
