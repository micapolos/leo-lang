package leo.base

data class Binary(
	val bit: EnumBit,
	val nextBinaryOrNull: Binary?) {
	override fun toString() = appendableString { it.append(this) }
}

fun Appendable.append(binary: Binary): Appendable =
	append("0b").appendBit(binary.bitStream)

val Binary.bitStream: Stream<EnumBit>
	get() =
		bit.onlyStream.then {
			nextBinaryOrNull?.bitStream
		}

fun Int.sizeBinaryOrNull(bit: EnumBit): Binary? =
	sizeStackOrNullOf(bit)?.reverseBinary

val Int.sizeMinBinaryOrNull: Binary?
	get() =
		sizeBinaryOrNull(EnumBit.ZERO)

val Int.sizeMaxBinaryOrNull: Binary?
	get() =
		sizeBinaryOrNull(EnumBit.ONE)

val Binary.zero: Binary
	get() =
		EnumBit.ZERO.binary.fold(bitStream.nextOrNull) {
			EnumBit.ZERO.append(this)
		}

val EnumBit.binary: Binary
	get() =
		Binary(this, null)

fun EnumBit.append(binaryOrNull: Binary?): Binary =
	Binary(this, binaryOrNull)

val Stack<EnumBit>.reverseBinary: Binary
	get() =
		head.binary.fold(tail) { bit -> bit.append(this) }

fun binary(bitInt: Int, vararg bitInts: Int): Binary =
	stack(bitInt, *bitInts.toTypedArray())
		.map(Int::enumBit)
		.reverseBinary

val Stream<EnumBit>.binary: Binary
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

val Long.binary: Binary
	get() =
		bitStream.binary

fun Int.binaryOrNullWithSize(size: Int): Binary? =
	(this to nullOf<Binary>()).iterate(size) {
		first.shr(1) to first.lastEnumBit.append(second)
	}.second

val Binary.clampedLong: Long
	get() =
		0L.fold(bitStream) { bit ->
			shl(1).or(bit.int.toLong())
		}

val Binary.clampedInt: Int
	get() =
		0.fold(bitStream) { bit ->
			toInt().shl(1).or(bit.int)
		}

// TODO: Check for overflow instead of clamping
val Binary.intOrNull: Int?
	get() =
		clampedInt

val Binary.clampedShort: Short
	get() =
		clampedInt.toShort()

val Binary.clampedByte: Byte
	get() =
		clampedShort.toByte()

val Binary?.carryIncrement: Pair<EnumBit, Binary?>
	get() =
		if (this == null) EnumBit.ONE to null
		else nextBinaryOrNull?.carryIncrement?.let { (carry, increment) ->
			when (bit) {
				EnumBit.ZERO ->
					when (carry) {
						EnumBit.ZERO -> EnumBit.ZERO to EnumBit.ZERO.append(increment)
						EnumBit.ONE -> EnumBit.ZERO to EnumBit.ONE.append(increment)
					}
				EnumBit.ONE ->
					when (carry) {
						EnumBit.ZERO -> EnumBit.ZERO to EnumBit.ONE.append(increment)
						EnumBit.ONE -> EnumBit.ONE to EnumBit.ZERO.append(increment)
					}
			}
		} ?: when (bit) {
			EnumBit.ZERO -> EnumBit.ZERO to EnumBit.ONE.binary
			EnumBit.ONE -> EnumBit.ONE to EnumBit.ZERO.binary
		}

val Binary?.incrementAndWrap: Binary?
	get() =
		carryIncrement.second

val Binary?.incrementAndGrow: Binary
	get() =
		carryIncrement.let { (carry, increment) ->
			when (carry) {
				EnumBit.ZERO -> increment!!
				EnumBit.ONE -> Binary(carry, increment)
			}
		}

val Binary?.incrementAndClamp: Binary?
	get() =
		carryIncrement.let { (carry, increment) ->
			when (carry) {
				EnumBit.ZERO -> increment
				EnumBit.ONE -> this
			}
		}

val Binary.increment: Binary?
	get() =
		carryIncrement.let { (carry, increment) ->
			when (carry) {
				EnumBit.ZERO -> increment
				EnumBit.ONE -> null
			}
		}

val Binary.borrowDecrement: Pair<EnumBit, Binary>
	get() =
		nextBinaryOrNull?.borrowDecrement?.let { (borrow, decrement) ->
			when (bit) {
				EnumBit.ZERO ->
					when (borrow) {
						EnumBit.ZERO -> EnumBit.ZERO to EnumBit.ZERO.append(decrement)
						EnumBit.ONE -> EnumBit.ONE to EnumBit.ONE.append(decrement)
					}
				EnumBit.ONE ->
					when (borrow) {
						EnumBit.ZERO -> EnumBit.ZERO to EnumBit.ONE.append(decrement)
						EnumBit.ONE -> EnumBit.ZERO to EnumBit.ZERO.append(decrement)
					}
			}
		} ?: when (bit) {
			EnumBit.ZERO -> EnumBit.ONE to EnumBit.ONE.binary
			EnumBit.ONE -> EnumBit.ZERO to EnumBit.ZERO.binary
		}

val Binary.decrement: Binary?
	get() =
		borrowDecrement.let { (borrow, decrement) ->
			when (borrow) {
				EnumBit.ZERO -> decrement
				EnumBit.ONE -> null
			}
		}

val Binary.decrementAndWrap: Binary?
	get() =
		borrowDecrement.second

val Binary.bitCountInt: Int
	get() =
		0.fold(bitStream) { inc() }

fun <V> Stream<V>.wrapIndexedStream(startIndexOrNull: Binary?): Stream<Indexed<V>> =
	startIndexOrNull.indexedTo(first).onlyStream.then {
		nextOrNull?.wrapIndexedStream(startIndexOrNull?.incrementAndWrap)
	}

tailrec fun <R> R.iterate(binary: Binary?, fn: R.() -> R): R =
	if (binary == null) this
	else fn().iterate(binary.decrement, fn)

//fun Binary.align(bitCountInt: Int): Binary =
//	(bitCountInt - this.bitCountInt).let { bitCountDelta ->
//		when {
//			bitCountDelta < 0 -> iterate(-bitCountDelta) { nextBinaryOrNull!! }
//			bitCountDelta > 0 -> iterate(bitCountDelta) { bitStack.push(0.bit).binary }
//			else -> this
//		}
//	}
