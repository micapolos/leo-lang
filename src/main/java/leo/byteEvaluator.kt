package leo

import leo.base.Bit
import leo.base.Stream
import leo.base.char

data class ByteEvaluator(
	val characterReader: Reader<Character>)

// === constructors

val emptyByteEvaluator =
	ByteEvaluator(emptyCharacterReader)

// === mutation

fun ByteEvaluator.evaluate(byte: Byte): ByteEvaluator? =
	if (ignoreWhitespaces && byte.char.isWhitespace()) this
	else byte.characterOrNull
		?.let(characterReader::read)
		?.let { copy(characterReader = it) }

fun ByteEvaluator.evaluateInternal(byte: Byte): Evaluator<Byte>? =
	evaluate(byte)?.evaluator

fun ByteEvaluator.apply(term: Term<Nothing>): Match? =
	characterReader.evaluator.applyFn(term)

// === byte stream

val ByteEvaluator.bitStreamOrNull: Stream<Bit>?
	get() =
		characterReader.bitStreamOrNull

val ByteEvaluator.evaluator: Evaluator<Byte>
	get() =
		Evaluator(
			this::evaluateInternal,
			this::apply,
			this::bitStreamOrNull)
