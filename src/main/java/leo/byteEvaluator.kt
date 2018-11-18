package leo

import leo.base.Bit
import leo.base.Stream

data class ByteEvaluator(
	val characterReader: CharacterReader)

// === constructors

val emptyByteEvaluator =
	ByteEvaluator(emptyCharacterReader)

// === mutation

fun ByteEvaluator.evaluate(byte: Byte): ByteEvaluator? =
	byte.characterOrNull
		?.let(characterReader::read)
		?.let { copy(characterReader = it) }

// === byte stream

val ByteEvaluator.bitStreamOrNull: Stream<Bit>?
	get() =
		characterReader.bitStreamOrNull

fun ByteEvaluator.apply(term: Term<Nothing>): Term<Nothing>? =
	characterReader.apply(term)