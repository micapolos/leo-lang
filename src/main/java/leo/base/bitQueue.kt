package leo.base

data class BitQueue(
	val bitArray: BitArray,
	val tailIndexBinary: Binary?,
	val headIndexBinary: Binary?)

val BitArray.bitQueue: BitQueue
	get() =
		BitQueue(
			this,
			minIndexBinaryOrNull,
			maxIndexBinaryOrNull)

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
