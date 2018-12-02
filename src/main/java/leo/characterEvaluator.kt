package leo

import leo.base.Bit
import leo.base.Stream
import leo.base.orNullThenIfNotNull

data class CharacterEvaluator(
	val tokenReader: Reader<Token<Nothing>>,
	val wordOrNull: Word?)

// === constructors

val emptyCharacterEvaluator =
	CharacterEvaluator(emptyTokenReader, null)

// === mutation

fun CharacterEvaluator.evaluate(character: Character): CharacterEvaluator? =
	when (character) {
		is BeginCharacter -> evaluateBegin
		is EndCharacter -> evaluateEnd
		is LetterCharacter -> plus(character.letter)
	}

fun CharacterEvaluator.evaluateInternal(character: Character): Evaluator<Character>? =
	evaluate(character)?.evaluator

fun CharacterEvaluator.apply(term: Term<Nothing>): Match? =
	tokenReader.evaluator.applyFn(term)

val CharacterEvaluator.evaluateBegin: CharacterEvaluator?
	get() =
		wordOrNull?.let { word ->
			tokenReader
				.read(word.token)
				?.read(begin.control.token)
				?.let { tokenReader ->
					CharacterEvaluator(tokenReader, null)
				}
		}

val CharacterEvaluator.evaluateEnd: CharacterEvaluator?
	get() =
		if (wordOrNull != null) null
		else tokenReader.read(end.control.token)?.let { endedTokenReader ->
			CharacterEvaluator(endedTokenReader, null)
		}

fun CharacterEvaluator.plus(letter: Letter): CharacterEvaluator =
	CharacterEvaluator(tokenReader, wordOrNull.orNullPlus(letter))

// === bit stream

val CharacterEvaluator.bitStreamOrNull: Stream<Bit>?
	get() =
		tokenReader.bitStreamOrNull.orNullThenIfNotNull {
			wordOrNull?.bitStream
		}

val CharacterEvaluator.evaluator: Evaluator<Character>
	get() =
		Evaluator(
			this::evaluateInternal,
			this::apply,
			this::bitStreamOrNull)
