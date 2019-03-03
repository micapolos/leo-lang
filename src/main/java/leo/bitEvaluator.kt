package leo

import leo.base.Stream
import leo.base.clampedByte
import leo.base.orNullThenIfNotNull
import leo.base.stream
import leo.binary.Bit
import leo.binary.clampedBit

data class BitEvaluator(
	val byteReader: Reader<Byte>,
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

fun BitEvaluator.evaluateInternal(bit: Bit): Evaluator<Bit>? =
	evaluate(bit)?.evaluator

fun BitEvaluator.apply(term: Term<Nothing>): Match? =
	byteReader.evaluator.applyFn(term)

val BitEvaluator.bitStreamOrNull: Stream<Bit>?
	get() =
		byteReader.bitStreamOrNull.orNullThenIfNotNull { partialByteBitStreamOrNull }

// TODO: This method is ugly, can we make it smarter?
val BitEvaluator.partialByteBitStreamOrNull: Stream<Bit>?
	get() =
		when (maskInt) {
			0x40 -> stream(byteInt.and(0x80).clampedBit)
			0x20 -> stream(byteInt.and(0x80).clampedBit, byteInt.and(0x40).clampedBit)
			0x10 -> stream(byteInt.and(0x80).clampedBit, byteInt.and(0x40).clampedBit, byteInt.and(0x20).clampedBit)
			0x08 -> stream(byteInt.and(0x80).clampedBit, byteInt.and(0x40).clampedBit, byteInt.and(0x20).clampedBit, byteInt.and(0x10).clampedBit)
			0x04 -> stream(byteInt.and(0x80).clampedBit, byteInt.and(0x40).clampedBit, byteInt.and(0x20).clampedBit, byteInt.and(0x10).clampedBit, byteInt.and(0x08).clampedBit)
			0x02 -> stream(byteInt.and(0x80).clampedBit, byteInt.and(0x40).clampedBit, byteInt.and(0x20).clampedBit, byteInt.and(0x10).clampedBit, byteInt.and(0x08).clampedBit, byteInt.and(0x04).clampedBit)
			0x01 -> stream(byteInt.and(0x80).clampedBit, byteInt.and(0x40).clampedBit, byteInt.and(0x20).clampedBit, byteInt.and(0x10).clampedBit, byteInt.and(0x08).clampedBit, byteInt.and(0x04).clampedBit, byteInt.and(0x02).clampedBit)
			else -> null
		}

// === transition to Evaluator<Bit>

val bitEvaluator =
	emptyBitEvaluator.evaluator

val BitEvaluator.evaluator: Evaluator<Bit>
	get() =
		Evaluator(
			this::evaluateInternal,
			this::apply,
			this::bitStreamOrNull)
