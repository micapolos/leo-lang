package leo.base

// Fixed-size POT cyclic bit buffer, with tail and head cursors, no overflow checks.
data class BitBuffer(
	val bitArray: BitArray,
	val tailIndexBinary: Binary?,
	val headIndexBinary: Binary?) {
	override fun toString() = appendableString { it.append(this) }
}

val BitArray.bitBuffer: BitBuffer
	get() =
		BitBuffer(this, minIndexBinaryOrNull, maxIndexBinaryOrNull)

fun Int.depthBitBuffer(bit: EnumBit): BitBuffer =
	depthBitArray(bit).bitBuffer

fun Appendable.append(bitBuffer: BitBuffer): Appendable =
	this
		.append('[')
		.pairTo(bitBuffer.headIndexBinary?.zero)
		.fold(bitBuffer.bitArray.bitStream) { bit ->
			let { (appendable, index) ->
				appendable
					.runIf(index == bitBuffer.tailIndexBinary) { append('<') }
					.append(bit)
					.runIf(index == bitBuffer.headIndexBinary) { append('>') }
					.pairTo(second?.incrementAndWrap)
			}
		}
		.first
		.append(']')

val BitBuffer.tailPop: BitBuffer
	get() =
		copy(tailIndexBinary = tailIndexBinary?.incrementAndWrap)

val BitBuffer.headPop: BitBuffer
	get() =
		copy(headIndexBinary = headIndexBinary?.decrementAndWrap)

val BitBuffer.tailPush: BitBuffer
	get() =
		copy(tailIndexBinary = tailIndexBinary?.decrementAndWrap)

val BitBuffer.headPush: BitBuffer
	get() =
		copy(headIndexBinary = headIndexBinary?.incrementAndWrap)

val BitBuffer.tailBit: EnumBit
	get() =
		bitArray[tailIndexBinary]!!.bitOrNull!!

val BitBuffer.headBit: EnumBit
	get() =
		bitArray[headIndexBinary]!!.bitOrNull!!

fun BitBuffer.tailSet(bit: EnumBit): BitBuffer =
	copy(bitArray = bitArray.set(tailIndexBinary, bit.bitArray)!!)

fun BitBuffer.headSet(bit: EnumBit): BitBuffer =
	copy(bitArray = bitArray.set(headIndexBinary, bit.bitArray)!!)