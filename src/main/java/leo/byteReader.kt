package leo

import leo.base.*

data class ByteReader(
	val byteEvaluator: ByteEvaluator,
	val termOrNull: Term<Nothing>?)

val emptyByteReader =
	ByteReader(emptyByteEvaluator, null)

fun ByteReader.read(byte: Byte): ByteReader? =
	this
		.termPush(leoReadField(byte))
		.termInvoke
		.termParse

fun ByteReader.termPush(field: Field<Nothing>): ByteReader =
	copy(termOrNull = termOrNull.push(field))

val ByteReader.termInvoke: ByteReader
	get() =
		if (termOrNull == null) this
		else copy(termOrNull = invoke(termOrNull))

val ByteReader.termParse: ByteReader?
	get() =
		copy(termOrNull = null).orNull
			.fold(termOrNull?.fieldStreamOrNull?.reverse) { field ->
				if (this == null) null
				else if (termOrNull != null) termPush(field)
				else if (field == leoWord fieldTo continueWord.term) this
				else field.leoReadByteOrNull.let { byte ->
					if (byte == null) termPush(field)
					else readPreprocessed(byte)
				}
			}

fun ByteReader.readPreprocessed(byte: Byte): ByteReader? =
	byteEvaluator.evaluate(byte)?.let { byteReader ->
		copy(byteEvaluator = byteReader)
	}

// === leo read bit

fun leoReadField(byte: Byte): Field<Nothing> =
	leoWord fieldTo term(readWord fieldTo byte.reflect.term)

val Field<Nothing>.leoReadByteOrNull: Byte?
	get() =
		get(leoWord)?.let { theLeoTerm ->
			theLeoTerm.value?.match(readWord) { readTerm ->
				readTerm?.match(byteWord) { byteTerm ->
					byteWord.fieldTo(byteTerm).parseByte
				}
			}
		}

// === bit stream

val ByteReader.bitStreamOrNull: Stream<Bit>?
	get() =
		byteEvaluator.bitStreamOrNull

fun ByteReader.invoke(term: Term<Nothing>) =
	byteEvaluator.invoke(term)