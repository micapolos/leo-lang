package leo.base

// Variable-size bit queue, backed by POT bit array,
data class BitQueue(
	val bitArray: BitArray,
	val tailIndexBinary: Binary?,
	val headIndexBinary: Binary?) {
	override fun toString() = appendableString { it.append(this) }
}

val BitArray.bitQueue: BitQueue
	get() =
		BitQueue(
			this,
			minIndexBinaryOrNull,
			maxIndexBinaryOrNull)

val Bit.bitQueue: BitQueue
	get() =
		bitArray.bitQueue

fun bitQueue(bitInt: Int, vararg bitInts: Int): BitQueue =
	bitInts.fold(bitInt.bit.bitQueue) { bitQueue, foldedBitInt ->
		bitQueue.enqueue(foldedBitInt.bit)
	}

val BitQueue.pushHead: BitQueue
	get() =
		headIndexBinary.orNullCarryIncrement.let { (carry, increment) ->
			if (carry == Bit.ZERO) copy(headIndexBinary = increment)
			else copy(
				bitArray = bitArray.incrementDepth!!,
				headIndexBinary = carry.append(increment),
				tailIndexBinary = Bit.ZERO.append(tailIndexBinary))
		}

val BitQueue?.orNullPushHead: BitQueue
	get() =
		this?.pushHead ?: bitQueue(0)

val BitQueue.popTail: BitQueue?
	get() =
		tailIndexBinary?.incrementAndWrap?.let { increment ->
			if (increment.bit == Bit.ZERO) copy(tailIndexBinary = increment)
			else copy(
				bitArray = bitArray.compositeOrNull!!.oneBitArray,
				headIndexBinary = headIndexBinary!!.nextBinaryOrNull,
				tailIndexBinary = increment.nextBinaryOrNull)
		}

fun BitQueue.setHead(bit: Bit): BitQueue =
	copy(bitArray = bitArray.set(headIndexBinary, bit.bitArray)!!)

val BitQueue.head: Bit
	get() =
		bitArray.get(headIndexBinary)!!.bitOrNull!!

fun BitQueue.setTail(bit: Bit): BitQueue =
	copy(bitArray = bitArray.set(tailIndexBinary, bit.bitArray)!!)

val BitQueue.tailBit: Bit
	get() =
		bitArray.get(tailIndexBinary)!!.bitOrNull!!

fun BitQueue.enqueue(bit: Bit): BitQueue =
	pushHead.setHead(bit)

val BitQueue.dequeue: Pair<Bit, BitQueue?>
	get() =
		popTail.let { tailBit.pairTo(it) }

fun BitQueue?.orNullEnqueue(bit: Bit): BitQueue =
	this?.enqueue(bit) ?: bit.bitQueue

val BitQueue.bitStream: Stream<Bit>
	get() =
		bitArray.indexedBitStream
			.dropWhile { indexOrNull != tailIndexBinary }!!
			.takeWhileInclusive { indexOrNull != headIndexBinary }
			.map(Indexed<Bit>::value)

fun Appendable.append(bitQueue: BitQueue): Appendable =
	this
		.append('<')
		.run { fold(bitQueue.bitStream, Appendable::append) }
		.append('>')
