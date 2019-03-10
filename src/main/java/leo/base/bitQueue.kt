package leo.base

// Non-empty bit queue, backed by power-of-two bit array
// enqueue, dequeue: O(log(n))
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

val EnumBit.bitQueue: BitQueue
	get() =
		bitArray.bitQueue

fun bitQueue(bitInt: Int, vararg bitInts: Int): BitQueue =
	bitInts.fold(bitInt.enumBit.bitQueue) { bitQueue, foldedBitInt ->
		bitQueue.enqueue(foldedBitInt.enumBit)
	}

val BitQueue.pushHead: BitQueue
	get() =
		headIndexBinary.carryIncrement.let { (carry, increment) ->
			if (carry == EnumBit.ZERO) copy(headIndexBinary = increment)
			else copy(
				bitArray = bitArray.incrementDepth,
				headIndexBinary = carry.append(increment),
				tailIndexBinary = EnumBit.ZERO.append(tailIndexBinary))
		}

val BitQueue?.orNullPushHead: BitQueue
	get() =
		this?.pushHead ?: bitQueue(0)

val BitQueue.popTail: BitQueue?
	get() =
		tailIndexBinary?.incrementAndWrap?.let { increment ->
			if (increment.bit == EnumBit.ZERO) copy(tailIndexBinary = increment)
			else copy(
				bitArray = bitArray.compositeOrNull!!.oneBitArray,
				headIndexBinary = headIndexBinary!!.nextBinaryOrNull,
				tailIndexBinary = increment.nextBinaryOrNull)
		}

fun BitQueue.setHead(bit: EnumBit): BitQueue =
	copy(bitArray = bitArray.set(headIndexBinary, bit.bitArray)!!)

val BitQueue.head: EnumBit
	get() =
		bitArray.get(headIndexBinary)!!.bitOrNull!!

fun BitQueue.setTail(bit: EnumBit): BitQueue =
	copy(bitArray = bitArray.set(tailIndexBinary, bit.bitArray)!!)

val BitQueue.tailBit: EnumBit
	get() =
		bitArray.get(tailIndexBinary)!!.bitOrNull!!

fun BitQueue.enqueue(bit: EnumBit): BitQueue =
	pushHead.setHead(bit)

val BitQueue.dequeue: Update<EnumBit, BitQueue?>
	get() =
		popTail.let { tailBit.andUpdated(it) }

fun BitQueue?.orNullEnqueue(bit: EnumBit): BitQueue =
	this?.enqueue(bit) ?: bit.bitQueue

val BitQueue.bitStream: Stream<EnumBit>
	get() =
		bitArray.indexedBitStream
			.dropWhile { indexOrNull != tailIndexBinary }!!
			.takeWhileInclusive { indexOrNull != headIndexBinary }
			.map(Indexed<EnumBit>::value)

fun Appendable.append(bitQueue: BitQueue): Appendable =
	this
		.append('<')
		.run { fold(bitQueue.bitStream, Appendable::append) }
		.append('>')
