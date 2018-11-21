package leo

import leo.base.*

data class BitReader(
	val bitEvaluator: BitEvaluator,
	val termOrNull: Term<Nothing>?)

val emptyBitReader =
	BitReader(emptyBitEvaluator, null)

fun BitReader.read(bit: Bit): BitReader? =
	if (!readersEnabled || !byteReaderEnabled) readPreprocessed(bit)
	else this
		.termPush(bit.reflect)
		.termInvoke(bit)

fun BitReader.termPush(field: Field<Nothing>): BitReader =
	copy(termOrNull = termOrNull.push(field))

fun BitReader.termInvoke(bit: Bit): BitReader? =
	if (termOrNull == null) this
	else termOrNull
		.push(leoWord fieldTo readWord.term)
		.let { argument ->
			function
				.get(argument)
				.let { matchOrNull ->
					if (matchOrNull == null)
						copy(termOrNull = null).readPreprocessed(bit)
					else when (matchOrNull.bodyBinaryTrieMatch) {
						is BinaryTrie.Match.Partial ->
							this // partial - continue
						is BinaryTrie.Match.Full ->
							copy(termOrNull = matchOrNull.bodyBinaryTrieMatch.value.apply(argument)).termParse
					}
				}
		}

val BitReader.termParse: BitReader?
	get() =
		copy(termOrNull = null).orNull
			.fold(termOrNull?.fieldStreamOrNull?.reverse) { field ->
				if (this == null) null
				else if (termOrNull != null) termPush(field)
				else field.parseBit.let { bitOrNull ->
					if (bitOrNull == null) termPush(field)
					else readPreprocessed(bitOrNull)
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

val BitReader.function: Function
	get() =
		bitEvaluator.function

// === transition to Reader<Bit>

val bitReader: Reader<Bit> =
	emptyBitReader.reader

val BitReader.reader: Reader<Bit>
	get() =
		Reader(
			Field<Nothing>::parseBit,
			Bit::reflect,
			bitEvaluator.evaluator,
			null)