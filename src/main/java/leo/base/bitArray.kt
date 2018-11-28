package leo.base

sealed class BitArray {
	abstract val depth: Int
}

data class LeafBitArray(
	val bit: Bit) : BitArray() {
	override val depth = 0
	override fun toString() = appendableString { it.append(this) }
}

data class InnerBitArray(
	val zeroBitArray: BitArray,
	val oneBitArray: BitArray) : BitArray() {
	override val depth = zeroBitArray.depth + 1
	override fun toString() = appendableString { it.append(this) }
}

val BitArray.grow: BitArray
	get() =
		InnerBitArray(this, this)

val Int.depthBitArray: BitArray
	get() =
		depthBitArrayOf(Bit.ZERO)

fun Int.depthBitArrayOf(bit: Bit): BitArray =
	if (this == 0)
		LeafBitArray(bit)
	else InnerBitArray(
		dec().depthBitArrayOf(bit),
		dec().depthBitArrayOf(bit))

fun bitArray(bit0: Bit): BitArray =
	LeafBitArray(bit0)

fun bitArray(bit0: Bit, bit1: Bit): BitArray =
	InnerBitArray(
		bitArray(bit0),
		bitArray(bit1))

fun bitArray(bit0: Bit, bit1: Bit, bit2: Bit, bit3: Bit): BitArray =
	InnerBitArray(
		bitArray(bit0, bit1),
		bitArray(bit2, bit3))

fun bitArray(bit0: Bit, bit1: Bit, bit2: Bit, bit3: Bit, bit4: Bit, bit5: Bit, bit6: Bit, bit7: Bit): BitArray =
	InnerBitArray(
		bitArray(bit0, bit1, bit2, bit3),
		bitArray(bit4, bit5, bit6, bit7))

// === casting

val BitArray.leafOrNull: LeafBitArray?
	get() =
		this as? LeafBitArray

val BitArray.innerOrNull: InnerBitArray?
	get() =
		this as? InnerBitArray

val BitArray.bitStream: Stream<Bit>
	get() =
		when (this) {
			is LeafBitArray -> bit.onlyStream
			is InnerBitArray -> zeroBitArray.bitStream.then { oneBitArray.bitStream }
		}

operator fun BitArray.get(bit: Bit): BitArray? =
	innerOrNull?.run {
		when (bit) {
			Bit.ZERO -> zeroBitArray
			Bit.ONE -> oneBitArray
		}
	}

fun Appendable.append(bitArray: BitArray): Appendable =
	this
		.append('[')
		.appendBit(bitArray.bitStream)
		.append(']')

operator fun BitArray.get(bitStream: Stream<Bit>): BitArray? =
	orNull.fold(bitStream) { bit ->
		this?.get(bit)
	}

operator fun BitArray.get(indexBitArray: BitArray): BitArray? =
	get(indexBitArray.bitStream)

operator fun BitArray.get(index: Binary): BitArray? =
	orNull.fold(index.bitStream) { bit ->
		this?.get(bit)
	}

operator fun BitArray.set(bit: Bit, bitArray: BitArray): BitArray? =
	innerOrNull?.run {
		assert(zeroBitArray.depth == bitArray.depth)
		when (bit) {
			Bit.ZERO -> InnerBitArray(bitArray, oneBitArray)
			Bit.ONE -> InnerBitArray(zeroBitArray, bitArray)
		}
	}

operator fun BitArray.set(bitStream: Stream<Bit>, bitArray: BitArray): BitArray? =
	bitStream.nextOrNull.let { nextBitStreamOrNull ->
		if (nextBitStreamOrNull == null)
			set(bitStream.first, bitArray)
		else get(bitStream.first)
			?.set(nextBitStreamOrNull, bitArray)
			?.let { updatedNextArray -> set(bitStream.first, updatedNextArray) }
	}

fun BitArray.map(fn: Bit.() -> Bit): BitArray =
	when (this) {
		is LeafBitArray -> fn(bit).bitArray
		is InnerBitArray -> InnerBitArray(zeroBitArray.map(fn), oneBitArray.map(fn))
	}

val BitArray.inverse: BitArray
	get() =
		map(Bit::inverse)

fun BitArray.set(bit: Bit): BitArray =
	map { bit }

operator fun BitArray.set(indexBitArray: BitArray, bitArray: BitArray): BitArray? =
	set(indexBitArray.bitStream, bitArray)

// === primitive to bit array

val Bit.bitArray: BitArray
	get() =
		LeafBitArray(this)

val Byte.bitArray: BitArray
	get() =
		InnerBitArray(
			InnerBitArray(
				InnerBitArray(
					bit7.bitArray,
					bit6.bitArray),
				InnerBitArray(
					bit5.bitArray,
					bit4.bitArray)),
			InnerBitArray(
				InnerBitArray(
					bit3.bitArray,
					bit2.bitArray),
				InnerBitArray(
					bit1.bitArray,
					bit0.bitArray)))

val Short.bitArray: BitArray
	get() =
		InnerBitArray(
			toInt().shr(8).toByte().bitArray,
			toByte().bitArray)

val Int.bitArray: BitArray
	get() =
		InnerBitArray(
			shr(16).toShort().bitArray,
			toShort().bitArray)

val Long.bitArray: BitArray
	get() =
		InnerBitArray(
			shr(32).toInt().bitArray,
			toInt().bitArray)

val Float.bitArray: BitArray
	get() =
		toRawBits().bitArray

val Double.bitArray: BitArray
	get() =
		toRawBits().bitArray

// === bit array to primitive

val BitArray.bitOrNull: Bit?
	get() =
		leafOrNull?.bit

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
