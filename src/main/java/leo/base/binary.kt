package leo.base

data class Binary(
	val bit: Bit,
	val nextBinaryOrNull: Binary?) {
	override fun toString() = appendableString { it.append(this) }
}

fun Appendable.append(binary: Binary): Appendable =
	append("0b").appendBit(binary.bitStream)

val Binary.bitStream: Stream<Bit>
	get() =
		bit.onlyStream.then {
			nextBinaryOrNull?.bitStream
		}

val Bit.binary: Binary
	get() =
		Binary(this, null)

fun Bit.append(binaryOrNull: Binary?): Binary =
	Binary(this, binaryOrNull)

val Stack<Bit>.reverseBinary: Binary
	get() =
		top.binary.fold(pop) { bit -> bit.append(this) }

fun binary(bitInt: Int, vararg bitInts: Int): Binary =
	stack(bitInt, *bitInts.toTypedArray())
		.map(Int::bit)
		.reverseBinary

val Stream<Bit>.binary: Binary
	get() =
		stack.reverseBinary

val Byte.binary: Binary
	get() =
		bitStream.binary

val Short.binary: Binary
	get() =
		bitStream.binary

val Int.binary: Binary
	get() =
		bitStream.binary

fun Int.binaryOrNullWithSize(size: Int): Binary? =
	(this to nullOf<Binary>()).iterate(size) {
		first.shr(1) to first.lastBit.append(second)
	}.second

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
		0.fold(bitStream) { inc() }

//fun Binary.align(bitCountInt: Int): Binary =
//	(bitCountInt - this.bitCountInt).let { bitCountDelta ->
//		when {
//			bitCountDelta < 0 -> iterate(-bitCountDelta) { nextBinaryOrNull!! }
//			bitCountDelta > 0 -> iterate(bitCountDelta) { bitStack.push(0.bit).binary }
//			else -> this
//		}
//	}
