package leo

import leo.base.Bit
import leo.base.Stream
import leo.base.orNullThenIfNotNull

data class CharacterEvaluator(
	val tokenReader: TokenReader,
	val wordOrNull: Word?)

// === constructors

val emptyCharacterEvaluator =
	CharacterEvaluator(emptyTokenReader, null)

// === mutation

fun CharacterEvaluator.evaluate(character: Character): CharacterEvaluator? =
	when (character) {
		is BeginCharacter -> begin
		is EndCharacter -> end
		is LetterCharacter -> plus(character.letter)
	}

val CharacterEvaluator.begin: CharacterEvaluator?
	get() =
		if (wordOrNull == null) null
		else tokenReader.read(BeginToken(wordOrNull))?.let { tokenReader ->
			CharacterEvaluator(tokenReader, null)
		}

val CharacterEvaluator.end: CharacterEvaluator?
	get() =
		if (wordOrNull != null) null
		else tokenReader.read(EndToken)?.let { endedTokenReader ->
			CharacterEvaluator(endedTokenReader, null)
		}

fun CharacterEvaluator.plus(letter: Letter): CharacterEvaluator =
	CharacterEvaluator(tokenReader, wordOrNull.plus(letter))

// === bit stream

val CharacterEvaluator.bitStreamOrNull: Stream<Bit>?
	get() =
		tokenReader.bitStreamOrNull
			.orNullThenIfNotNull(wordOrNull?.bitStream)

fun CharacterEvaluator.apply(term: Term<Nothing>): Term<Nothing>? =
	tokenReader.apply(term)