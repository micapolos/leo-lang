package leo.base

data class Binary(
	val bitStack: Stack<Bit>) {
	override fun toString() = appendableString { it.append(this) }
}

fun Appendable.append(binary: Binary): Appendable =
	append("0b").appendBit(binary.bitStream)

val Binary.bitStream: Stream<Bit>
	get() =
		bitStack.reverse.stream

val Int.sizeZeroBinaryOrNull: Binary?
	get() =
		sizeStackOrNullOf(Bit.ZERO)?.binary

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

fun binary(bit: Bit, vararg bits: Bit): Binary =
	stack(bit, *bits).binary

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

// TODO: Replace with proper math!!!
val Binary.increment: Binary?
	get() = clampedInt.inc().binary

val Binary.bitCountInt: Int
	get() =
		bitStack.sizeInt

fun Binary.align(bitCountInt: Int): Binary =
	(bitCountInt - this.bitCountInt).let { bitCountDelta ->
		when {
			bitCountDelta < 0 -> iterate(-bitCountDelta) { bitStack.pop!!.binary }
			bitCountDelta > 0 -> iterate(bitCountDelta) { bitStack.push(0.bit).binary }
			else -> this
		}
	}
