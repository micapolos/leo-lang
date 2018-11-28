package leo.base

sealed class BitArray {
	abstract val depth: Int
}

data class SingleBitArray(
	val bit: Bit) : BitArray() {
	override val depth = 0
	override fun toString() = appendableString { it.append(this) }
}

data class CompositeBitArray(
	val zeroBitArray: BitArray,
	val oneBitArray: BitArray) : BitArray() {
	override val depth = zeroBitArray.depth + 1
	override fun toString() = appendableString { it.append(this) }
}

val BitArray.grow: BitArray
	get() =
		CompositeBitArray(this, this)

val Int.depthBitArray: BitArray
	get() =
		depthBitArrayOf(Bit.ZERO)

fun Int.depthBitArrayOf(bit: Bit): BitArray =
	if (this == 0)
		SingleBitArray(bit)
	else CompositeBitArray(
		dec().depthBitArrayOf(bit),
		dec().depthBitArrayOf(bit))

fun bitArray(bit0: Bit): BitArray =
	SingleBitArray(bit0)

fun bitArray(bit0: Bit, bit1: Bit): BitArray =
	CompositeBitArray(
		bitArray(bit0),
		bitArray(bit1))

fun bitArray(bit0: Bit, bit1: Bit, bit2: Bit, bit3: Bit): BitArray =
	CompositeBitArray(
		bitArray(bit0, bit1),
		bitArray(bit2, bit3))

fun bitArray(bit0: Bit, bit1: Bit, bit2: Bit, bit3: Bit, bit4: Bit, bit5: Bit, bit6: Bit, bit7: Bit): BitArray =
	CompositeBitArray(
		bitArray(bit0, bit1, bit2, bit3),
		bitArray(bit4, bit5, bit6, bit7))

// === casting

val BitArray.singleOrNull: SingleBitArray?
	get() =
		this as? SingleBitArray

val BitArray.compositeOrNull: CompositeBitArray?
	get() =
		this as? CompositeBitArray

val BitArray.bitStream: Stream<Bit>
	get() =
		when (this) {
			is SingleBitArray -> bit.onlyStream
			is CompositeBitArray -> zeroBitArray.bitStream.then { oneBitArray.bitStream }
		}

operator fun BitArray.get(bit: Bit): BitArray? =
	compositeOrNull?.run {
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

operator fun BitArray.get(index: Binary): BitArray? =
	orNull.fold(index.bitStream) { bit ->
		this?.get(bit)
	}

operator fun BitArray.set(bit: Bit, bitArray: BitArray): BitArray? =
	compositeOrNull?.run {
		assert(zeroBitArray.depth == bitArray.depth)
		when (bit) {
			Bit.ZERO -> CompositeBitArray(bitArray, oneBitArray)
			Bit.ONE -> CompositeBitArray(zeroBitArray, bitArray)
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

// === mapping

fun BitArray.map(fn: Bit.() -> Bit): BitArray =
	when (this) {
		is SingleBitArray -> fn(bit).bitArray
		is CompositeBitArray -> CompositeBitArray(zeroBitArray.map(fn), oneBitArray.map(fn))
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

val BitArray.bitOrNull: Bit?
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
