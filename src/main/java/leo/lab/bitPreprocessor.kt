package leo.lab

import leo.*
import leo.base.*

data class BitPreprocessor(
	val bitReader: BitReader,
	val termOrNull: Term<Nothing>?)

val emptyBitPreprocessor =
	BitPreprocessor(emptyBitReader, null)

fun BitPreprocessor.plus(bit: Bit): BitPreprocessor? =
	this
		.termPush(leoReadField(bit))
		.termInvoke
		.termParse

fun BitPreprocessor.termPush(field: Field<Nothing>): BitPreprocessor =
	copy(termOrNull = termOrNull.push(field))

val BitPreprocessor.termInvoke: BitPreprocessor
	get() =
		if (termOrNull == null) this
		else copy(termOrNull = bitReader.byteReader.tokenReader.scope.function.invoke(termOrNull))

val BitPreprocessor.termParse: BitPreprocessor?
	get() =
		copy(termOrNull = null).orNull
			.fold(termOrNull?.fieldStreamOrNull) { field ->
				if (this == null) null
				else if (termOrNull != null) termPush(field)
				else field.leoReadBitOrNull.let { bit ->
					if (bit == null) termPush(field)
					else readPreprocessed(bit)
				}
			}

fun BitPreprocessor.readPreprocessed(bit: Bit): BitPreprocessor? =
	bitReader.read(bit)?.let { bitReader ->
		copy(bitReader = bitReader)
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

val BitPreprocessor.bitSteamOrNull: Stream<Bit>?
	get() =
		termWord.bitStream
			.then { beginBitStream }
			.then { bitReader.bitStreamOrNull }
			.then { endBitStream }
			.then { readerWord.bitStream }
			.then { beginBitStream }
			.then { termOrNull?.bitStream }
			.then { endBitStream }