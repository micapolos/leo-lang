package leo

import leo.base.*

data class BitReader(
	val bitEvaluator: BitEvaluator,
	val termOrNull: Term<Nothing>?)

val emptyBitReader =
	BitReader(emptyBitEvaluator, null)

fun BitReader.read(bit: Bit): BitReader? =
	this
		.termPush(leoReadField(bit))
		.termInvoke
		.termParse

fun BitReader.termPush(field: Field<Nothing>): BitReader =
	copy(termOrNull = termOrNull.push(field))

val BitReader.termInvoke: BitReader
	get() =
		if (termOrNull == null) this
		else copy(termOrNull = invoke(termOrNull))

val BitReader.termParse: BitReader?
	get() =
		copy(termOrNull = null).orNull
			.fold(termOrNull?.fieldStreamOrNull?.reverse) { field ->
				if (this == null) null
				else if (termOrNull != null) termPush(field)
				else if (field == leoWord fieldTo continueWord.term) this
				else field.leoReadBitOrNull.let { bit ->
					if (bit == null) termPush(field)
					else readPreprocessed(bit)
				}
			}

fun BitReader.readPreprocessed(bit: Bit): BitReader? =
	bitEvaluator.evaluate(bit)?.let { bitReader ->
		copy(bitEvaluator = bitReader)
	}

// === leo read bit

fun leoReadField(bit: Bit): Field<Nothing> =
	leoWord fieldTo term(readWord fieldTo bit.reflect.term)

val Field<Nothing>.leoReadBitOrNull: Bit?
	get() =
		get(leoWord)?.let { theLeoTerm ->
			theLeoTerm.value?.match(readWord) { readTerm ->
				readTerm?.match(bitWord) { bitTerm ->
					bitWord.fieldTo(bitTerm).parseBit
				}
			}
		}

// === bit stream

val BitReader.bitStreamOrNull: Stream<Bit>?
	get() =
		bitEvaluator.bitStreamOrNull

val BitReader.partialByteBitStreamOrNull: Stream<Bit>?
	get() =
		bitEvaluator.partialByteBitStreamOrNull

fun BitReader.invoke(term: Term<Nothing>) =
	bitEvaluator.invoke(term)