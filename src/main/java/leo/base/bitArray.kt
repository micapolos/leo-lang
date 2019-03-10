package leo.base

// Non-empty, power-of-two bit array
// random access: O(log(n)) for bits and power-of-two aligned sub-arrays
sealed class BitArray {
	abstract val depth: Int
}

data class SingleBitArray(
	val bit: EnumBit) : BitArray() {
	override val depth = 0
	override fun toString() = appendableString { it.append(this) }
}

data class CompositeBitArray(
	val zeroBitArray: BitArray,
	val oneBitArray: BitArray) : BitArray() {
	override val depth = zeroBitArray.depth + 1
	override fun toString() = appendableString { it.append(this) }
}

val BitArray.indexBitCount: BitCount
	get() =
		when (this) {
			is SingleBitArray -> bitBitCount
			is CompositeBitArray -> zeroBitArray.indexBitCount.increment
		}

val BitArray.incrementDepth: BitArray
	get() =
		CompositeBitArray(this, this)

val BitArray.minIndexBinaryOrNull: Binary?
	get() =
		depth.sizeMinBinaryOrNull

val BitArray.maxIndexBinaryOrNull: Binary?
	get() =
		depth.sizeMaxBinaryOrNull

fun BitArray.increaseDepth(depth: Int): BitArray? =
	(depth - this.depth).let { delta ->
		if (delta > 0) orNull.iterate(delta) { this?.incrementDepth }
		else this
	}

val Int.depthBitArray: BitArray
	get() =
		depthBitArray(EnumBit.ZERO)

fun Int.depthBitArray(bit: EnumBit): BitArray =
	if (this == 0)
		SingleBitArray(bit)
	else CompositeBitArray(
		dec().depthBitArray(bit),
		dec().depthBitArray(bit))

fun bitArray(bitInt0: Int): BitArray =
	SingleBitArray(bitInt0.enumBit)

fun bitArray(bitInt0: Int, bitInt1: Int): BitArray =
	CompositeBitArray(
		bitArray(bitInt0),
		bitArray(bitInt1))

fun bitArray(bitInt0: Int, bitInt1: Int, bitInt2: Int, bitInt3: Int): BitArray =
	CompositeBitArray(
		bitArray(bitInt0, bitInt1),
		bitArray(bitInt2, bitInt3))

fun bitArray(
	bitInt0: Int, bitInt1: Int, bitInt2: Int, bitInt3: Int,
	bitInt4: Int, bitInt5: Int, bitInt6: Int, bitInt7: Int): BitArray =
	CompositeBitArray(
		bitArray(bitInt0, bitInt1, bitInt2, bitInt3),
		bitArray(bitInt4, bitInt5, bitInt6, bitInt7))

// === casting

val BitArray.singleOrNull: SingleBitArray?
	get() =
		this as? SingleBitArray

val BitArray.compositeOrNull: CompositeBitArray?
	get() =
		this as? CompositeBitArray

val BitArray.bitStream: Stream<EnumBit>
	get() =
		when (this) {
			is SingleBitArray -> bit.onlyStream
			is CompositeBitArray -> zeroBitArray.bitStream.then { oneBitArray.bitStream }
		}

operator fun BitArray.get(bit: EnumBit): BitArray? =
	compositeOrNull?.run {
		when (bit) {
			EnumBit.ZERO -> zeroBitArray
			EnumBit.ONE -> oneBitArray
		}
	}

fun Appendable.append(bitArray: BitArray): Appendable =
	this
		.append('[')
		.appendBit(bitArray.bitStream)
		.append(']')

operator fun BitArray.get(bitStream: Stream<EnumBit>?): BitArray? =
	orNull.fold(bitStream) { bit ->
		this?.get(bit)
	}

operator fun BitArray.get(index: Binary?): BitArray? =
	get(index?.bitStream)

fun BitArray.set(bitArray: BitArray): BitArray? =
	if (depth != bitArray.depth) null
	else bitArray

operator fun BitArray.set(bit: EnumBit, bitArray: BitArray): BitArray? =
	compositeOrNull?.run {
		if (zeroBitArray.depth != bitArray.depth) null
		else when (bit) {
			EnumBit.ZERO -> CompositeBitArray(bitArray, oneBitArray)
			EnumBit.ONE -> CompositeBitArray(zeroBitArray, bitArray)
		}
	}

operator fun BitArray.set(bitStreamOrNull: Stream<EnumBit>?, bitArray: BitArray): BitArray? =
	if (bitStreamOrNull == null) set(bitArray)
	else bitStreamOrNull.nextOrNull.let { nextBitStreamOrNull ->
		if (nextBitStreamOrNull == null)
			set(bitStreamOrNull.first, bitArray)
		else get(bitStreamOrNull.first)
			?.set(nextBitStreamOrNull, bitArray)
			?.let { updatedNextArray -> set(bitStreamOrNull.first, updatedNextArray) }
	}

operator fun BitArray.set(binary: Binary?, bitArray: BitArray): BitArray? =
	set(binary?.bitStream, bitArray)

// === indexed stream

val BitArray.indexedBitStream: Stream<Indexed<EnumBit>>
	get() =
		bitStream.wrapIndexedStream(minIndexBinaryOrNull)

// === mapping

fun BitArray.map(fn: EnumBit.() -> EnumBit): BitArray =
	when (this) {
		is SingleBitArray -> fn(bit).bitArray
		is CompositeBitArray -> CompositeBitArray(zeroBitArray.map(fn), oneBitArray.map(fn))
	}

val BitArray.inverse: BitArray
	get() =
		map(EnumBit::inverse)

fun BitArray.set(bit: EnumBit): BitArray =
	map { bit }

// === primitive to bit array

val EnumBit.bitArray: BitArray
	get() =
		SingleBitArray(this)

val Byte.bitArray: BitArray
	get() =
		CompositeBitArray(
			CompositeBitArray(
				CompositeBitArray(
					bit7.bitArray,
					bit6.bitArray),
				CompositeBitArray(
					bit5.bitArray,
					bit4.bitArray)),
			CompositeBitArray(
				CompositeBitArray(
					bit3.bitArray,
					bit2.bitArray),
				CompositeBitArray(
					bit1.bitArray,
					bit0.bitArray)))

val Short.bitArray: BitArray
	get() =
		CompositeBitArray(
			toInt().shr(8).toByte().bitArray,
			toByte().bitArray)

val Int.bitArray: BitArray
	get() =
		CompositeBitArray(
			shr(16).toShort().bitArray,
			toShort().bitArray)

val Long.bitArray: BitArray
	get() =
		CompositeBitArray(
			shr(32).toInt().bitArray,
			toInt().bitArray)

val Float.bitArray: BitArray
	get() =
		toRawBits().bitArray

val Double.bitArray: BitArray
	get() =
		toRawBits().bitArray

// === bit array to primitive

val BitArray.bitOrNull: EnumBit?
	get() =
		singleOrNull?.bit

val BitArray.byteOrNull: Byte?
	get() =
		if (depth != 3) null
		else 0.fold(bitStream) { bit ->
			shl(1).or(bit.int)
		}.toByte()

val BitArray.shortOrNull: Short?
	get() =
		if (depth != 4) null
		else 0.fold(bitStream) { bit ->
			shl(1).or(bit.int)
		}.toShort()

val BitArray.intOrNull: Int?
	get() =
		if (depth != 5) null
		else 0.fold(bitStream) { bit ->
			shl(1).or(bit.int)
		}

val BitArray.longOrNull: Long?
	get() =
		if (depth != 6) null
		else 0L.fold(bitStream) { bit ->
			shl(1).or(bit.int.toLong())
		}

val BitArray.floatOrNull: Float?
	get() =
		intOrNull?.let { Float.fromBits(it) }

val BitArray.doubleOrNull: Double?
	get() =
		longOrNull?.let { Double.fromBits(it) }

