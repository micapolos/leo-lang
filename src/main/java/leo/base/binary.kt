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

fun Int.sizeBinaryOrNull(bit: Bit): Binary? =
	sizeStackOrNullOf(bit)?.reverseBinary

val Int.sizeMinBinaryOrNull: Binary?
	get() =
		sizeBinaryOrNull(Bit.ZERO)

val Int.sizeMaxBinaryOrNull: Binary?
	get() =
		sizeBinaryOrNull(Bit.ONE)

val Binary.zero: Binary
	get() =
		Bit.ZERO.binary.fold(bitStream.nextOrNull) {
			Bit.ZERO.append(this)
		}

val Bit.binary: Binary
	get() =
		Binary(this, null)

fun Bit.append(binaryOrNull: Binary?): Binary =
	Binary(this, binaryOrNull)

val Stack<Bit>.reverseBinary: Binary
	get() =
		head.binary.fold(tail) { bit -> bit.append(this) }

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

val Long.binary: Binary
	get() =
		bitStream.binary

fun Int.binaryOrNullWithSize(size: Int): Binary? =
	(this to nullOf<Binary>()).iterate(size) {
		first.shr(1) to first.lastBit.append(second)
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

val Binary?.carryIncrement: Pair<Bit, Binary?>
	get() =
		if (this == null) Bit.ONE to null
		else nextBinaryOrNull?.carryIncrement?.let { (carry, increment) ->
			when (bit) {
				Bit.ZERO ->
					when (carry) {
						Bit.ZERO -> Bit.ZERO to Bit.ZERO.append(increment)
						Bit.ONE -> Bit.ZERO to Bit.ONE.append(increment)
					}
				Bit.ONE ->
					when (carry) {
						Bit.ZERO -> Bit.ZERO to Bit.ONE.append(increment)
						Bit.ONE -> Bit.ONE to Bit.ZERO.append(increment)
					}
			}
		} ?: when (bit) {
			Bit.ZERO -> Bit.ZERO to Bit.ONE.binary
			Bit.ONE -> Bit.ONE to Bit.ZERO.binary
		}

val Binary?.incrementAndWrap: Binary?
	get() =
		carryIncrement.second

val Binary?.incrementAndGrow: Binary
	get() =
		carryIncrement.let { (carry, increment) ->
			when (carry) {
				Bit.ZERO -> increment!!
				Bit.ONE -> Binary(carry, increment)
			}
		}

val Binary?.incrementAndClamp: Binary?
	get() =
		carryIncrement.let { (carry, increment) ->
			when (carry) {
				Bit.ZERO -> increment
				Bit.ONE -> this
			}
		}

val Binary.increment: Binary?
	get() =
		carryIncrement.let { (carry, increment) ->
			when (carry) {
				Bit.ZERO -> increment
				Bit.ONE -> null
			}
		}

val Binary.borrowDecrement: Pair<Bit, Binary>
	get() =
		nextBinaryOrNull?.borrowDecrement?.let { (borrow, decrement) ->
			when (bit) {
				Bit.ZERO ->
					when (borrow) {
						Bit.ZERO -> Bit.ZERO to Bit.ZERO.append(decrement)
						Bit.ONE -> Bit.ONE to Bit.ONE.append(decrement)
					}
				Bit.ONE ->
					when (borrow) {
						Bit.ZERO -> Bit.ZERO to Bit.ONE.append(decrement)
						Bit.ONE -> Bit.ZERO to Bit.ZERO.append(decrement)
					}
			}
		} ?: when (bit) {
			Bit.ZERO -> Bit.ONE to Bit.ONE.binary
			Bit.ONE -> Bit.ZERO to Bit.ZERO.binary
		}

val Binary.decrement: Binary?
	get() =
		borrowDecrement.let { (borrow, decrement) ->
			when (borrow) {
				Bit.ZERO -> decrement
				Bit.ONE -> null
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

//fun Binary.align(bitCountInt: Int): Binary =
//	(bitCountInt - this.bitCountInt).let { bitCountDelta ->
//		when {
//			bitCountDelta < 0 -> iterate(-bitCountDelta) { nextBinaryOrNull!! }
//			bitCountDelta > 0 -> iterate(bitCountDelta) { bitStack.push(0.bit).binary }
//			else -> this
//		}
//	}
