package leo

import leo.base.*

data class ByteReader(
	val byteEvaluator: ByteEvaluator,
	val termOrNull: Term<Nothing>?)

val emptyByteReader =
	ByteReader(emptyByteEvaluator, null)

fun ByteReader.read(byte: Byte): ByteReader? =
	if (byteReaderIgnoreWhiteSpaces && byte.toChar().isWhitespace()) this
	else if (!readersEnabled || !byteReaderEnabled) readPreprocessed(byte)
	else this
		.termPush(byte.reflect)
		.termInvoke(byte)

fun ByteReader.termPush(field: Field<Nothing>): ByteReader =
	copy(termOrNull = termOrNull.push(field))

fun ByteReader.termInvoke(byte: Byte): ByteReader? =
		if (termOrNull == null) this
		else termOrNull
			.push(leoWord fieldTo readWord.term)
			.let { argument ->
				function
					.get(argument)
					.let { matchOrNull ->
						if (matchOrNull == null)
							copy(termOrNull = null).readPreprocessed(byte)
						else when (matchOrNull.bodyBinaryTrieMatch) {
							is BinaryTrie.Match.Partial ->
								this // partial - continue
							is BinaryTrie.Match.Full ->
								copy(termOrNull = matchOrNull.bodyBinaryTrieMatch.value.apply(argument)).termParse
						}
					}
			}

val ByteReader.termParse: ByteReader?
	get() =
		copy(termOrNull = null).orNull
			.fold(termOrNull?.fieldStreamOrNull?.reverse) { field ->
				if (this == null) null
				else if (termOrNull != null) termPush(field)
				else field.parseByte.let { byteOrNull ->
					if (byteOrNull == null) termPush(field)
					else readPreprocessed(byteOrNull)
				}
			}

fun ByteReader.readPreprocessed(byte: Byte): ByteReader? =
	byteEvaluator.evaluate(byte)?.let { byteReader ->
		copy(byteEvaluator = byteReader)
	}

// === bit stream

val ByteReader.bitStreamOrNull: Stream<Bit>?
	get() =
		byteEvaluator.bitStreamOrNull

val ByteReader.function: Function
	get() =
		byteEvaluator.function