package leo.lab

import leo.base.Bit
import leo.base.Stream
import leo.base.bit
import leo.base.stream

data class BitEvaluator(
	val byteReader: ByteReader,
	val byteInt: Int,
	val maskInt: Int)

val emptyBitEvaluator =
	BitEvaluator(emptyByteReader, 0, 0x80)

fun BitEvaluator.read(bit: Bit): BitEvaluator? {
	val nextByteInt = byteInt.or(if (bit == Bit.ONE) maskInt else 0)
	val nextMaskInt = maskInt.shr(1)
	return if (nextMaskInt == 0) {
		byteReader.plus(nextByteInt.toByte())?.let { nextByteReader ->
			BitEvaluator(nextByteReader, 0, 0x80)
		}
	} else {
		BitEvaluator(byteReader, nextByteInt, nextMaskInt)
	}
}

val BitEvaluator.bitStreamOrNull: Stream<Bit>?
	get() =
		byteReader.bitStreamOrNull

// TODO: This method is ugly, can we make it smarter?
val BitEvaluator.partialByteBitStreamOrNull: Stream<Bit>?
	get() =
		when (maskInt) {
			0x80 -> null
			0x40 -> stream(byteInt.and(0x80).bit)
			0x20 -> stream(byteInt.and(0x80).bit, byteInt.and(0x40).bit)
			0x10 -> stream(byteInt.and(0x80).bit, byteInt.and(0x40).bit, byteInt.and(0x20).bit)
			0x08 -> stream(byteInt.and(0x80).bit, byteInt.and(0x40).bit, byteInt.and(0x20).bit, byteInt.and(0x10).bit)
			0x04 -> stream(byteInt.and(0x80).bit, byteInt.and(0x40).bit, byteInt.and(0x20).bit, byteInt.and(0x10).bit, byteInt.and(0x08).bit)
			0x02 -> stream(byteInt.and(0x80).bit, byteInt.and(0x40).bit, byteInt.and(0x20).bit, byteInt.and(0x10).bit, byteInt.and(0x08).bit, byteInt.and(0x04).bit)
			0x01 -> stream(byteInt.and(0x80).bit, byteInt.and(0x40).bit, byteInt.and(0x20).bit, byteInt.and(0x10).bit, byteInt.and(0x08).bit, byteInt.and(0x04).bit, byteInt.and(0x02).bit)
			else -> null
		}

fun BitEvaluator.invoke(term: Term<Nothing>) =
	byteReader.invoke(term)