package leo

import leo.base.*

data class BitEvaluator(
	val byteReader: Reader<Byte>,
	val byteInt: Int,
	val maskInt: Int)

val emptyBitEvaluator =
	BitEvaluator(emptyByteReader, 0, 0x80)

fun BitEvaluator.evaluate(bit: EnumBit): BitEvaluator? {
	val nextByteInt = byteInt.or(if (bit == EnumBit.ONE) maskInt else 0)
	val nextMaskInt = maskInt.shr(1)
	return if (nextMaskInt == 0) {
		byteReader.read(nextByteInt.clampedByte)?.let { nextByteReader ->
			BitEvaluator(nextByteReader, 0, 0x80)
		}
	} else {
		BitEvaluator(byteReader, nextByteInt, nextMaskInt)
	}
}

fun BitEvaluator.evaluateInternal(bit: EnumBit): Evaluator<EnumBit>? =
	evaluate(bit)?.evaluator

fun BitEvaluator.apply(term: Term<Nothing>): Match? =
	byteReader.evaluator.applyFn(term)

val BitEvaluator.bitStreamOrNull: Stream<EnumBit>?
	get() =
		byteReader.bitStreamOrNull.orNullThenIfNotNull { partialByteBitStreamOrNull }

// TODO: This method is ugly, can we make it smarter?
val BitEvaluator.partialByteBitStreamOrNull: Stream<EnumBit>?
	get() =
		when (maskInt) {
			0x40 -> stream(byteInt.and(0x80).clampedEnumBit)
			0x20 -> stream(byteInt.and(0x80).clampedEnumBit, byteInt.and(0x40).clampedEnumBit)
			0x10 -> stream(byteInt.and(0x80).clampedEnumBit, byteInt.and(0x40).clampedEnumBit, byteInt.and(0x20).clampedEnumBit)
			0x08 -> stream(byteInt.and(0x80).clampedEnumBit, byteInt.and(0x40).clampedEnumBit, byteInt.and(0x20).clampedEnumBit, byteInt.and(0x10).clampedEnumBit)
			0x04 -> stream(byteInt.and(0x80).clampedEnumBit, byteInt.and(0x40).clampedEnumBit, byteInt.and(0x20).clampedEnumBit, byteInt.and(0x10).clampedEnumBit, byteInt.and(0x08).clampedEnumBit)
			0x02 -> stream(byteInt.and(0x80).clampedEnumBit, byteInt.and(0x40).clampedEnumBit, byteInt.and(0x20).clampedEnumBit, byteInt.and(0x10).clampedEnumBit, byteInt.and(0x08).clampedEnumBit, byteInt.and(0x04).clampedEnumBit)
			0x01 -> stream(byteInt.and(0x80).clampedEnumBit, byteInt.and(0x40).clampedEnumBit, byteInt.and(0x20).clampedEnumBit, byteInt.and(0x10).clampedEnumBit, byteInt.and(0x08).clampedEnumBit, byteInt.and(0x04).clampedEnumBit, byteInt.and(0x02).clampedEnumBit)
			else -> null
		}

// === transition to Evaluator<Bit>

val bitEvaluator =
	emptyBitEvaluator.evaluator

val BitEvaluator.evaluator: Evaluator<EnumBit>
	get() =
		Evaluator(
			this::evaluateInternal,
			this::apply,
			this::bitStreamOrNull)
