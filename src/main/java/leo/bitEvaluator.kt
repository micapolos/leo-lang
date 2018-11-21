package leo

import leo.base.*

data class BitEvaluator(
	val byteReader: ByteReader,
	val byteInt: Int,
	val maskInt: Int)

val emptyBitEvaluator =
	BitEvaluator(emptyByteReader, 0, 0x80)

fun BitEvaluator.evaluate(bit: Bit): BitEvaluator? {
	val nextByteInt = byteInt.or(if (bit == Bit.ONE) maskInt else 0)
	val nextMaskInt = maskInt.shr(1)
	return if (nextMaskInt == 0) {
		byteReader.read(nextByteInt.clampedByte)?.let { nextByteReader ->
			BitEvaluator(nextByteReader, 0, 0x80)
		}
	} else {
		BitEvaluator(byteReader, nextByteInt, nextMaskInt)
	}
}

val BitEvaluator.bitStreamOrNull: Stream<Bit>?
	get() =
		byteReader.bitStreamOrNull?.then { partialByteBitStreamOrNull }

// TODO: This method is ugly, can we make it smarter?
val BitEvaluator.partialByteBitStreamOrNull: Stream<Bit>?
	get() =
		when (maskInt) {
			0x80 -> null
			0x40 -> stream(byteInt.and(0x80).clampedBit)
			0x20 -> stream(byteInt.and(0x80).clampedBit, byteInt.and(0x40).clampedBit)
			0x10 -> stream(byteInt.and(0x80).clampedBit, byteInt.and(0x40).clampedBit, byteInt.and(0x20).clampedBit)
			0x08 -> stream(byteInt.and(0x80).clampedBit, byteInt.and(0x40).clampedBit, byteInt.and(0x20).clampedBit, byteInt.and(0x10).clampedBit)
			0x04 -> stream(byteInt.and(0x80).clampedBit, byteInt.and(0x40).clampedBit, byteInt.and(0x20).clampedBit, byteInt.and(0x10).clampedBit, byteInt.and(0x08).clampedBit)
			0x02 -> stream(byteInt.and(0x80).clampedBit, byteInt.and(0x40).clampedBit, byteInt.and(0x20).clampedBit, byteInt.and(0x10).clampedBit, byteInt.and(0x08).clampedBit, byteInt.and(0x04).clampedBit)
			0x01 -> stream(byteInt.and(0x80).clampedBit, byteInt.and(0x40).clampedBit, byteInt.and(0x20).clampedBit, byteInt.and(0x10).clampedBit, byteInt.and(0x08).clampedBit, byteInt.and(0x04).clampedBit, byteInt.and(0x02).clampedBit)
			else -> null
		}

val BitEvaluator.function: Function
	get() =
		byteReader.function

// === transition to Evaluator<Bit>

val bitEvaluator =
	emptyBitEvaluator.evaluator

val BitEvaluator.evaluator: Evaluator<Bit>
	get() =
		Evaluator(
			{ bit ->
				evaluate(bit)?.evaluator
			},
			{ function.get(it) },
			this::bitStreamOrNull)
