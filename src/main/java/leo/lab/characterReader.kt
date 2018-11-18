package leo.lab

import leo.*
import leo.base.Bit
import leo.base.Stream
import leo.base.fold
import leo.base.orNull

data class CharacterReader(
	val characterEvaluator: CharacterEvaluator,
	val termOrNull: Term<Nothing>?)

val emptyCharacterReader =
	CharacterReader(emptyCharacterEvaluator, null)

fun CharacterReader.read(character: Character): CharacterReader? =
	this
		.termPush(leoReadField(character))
		.termInvoke
		.termParse

fun CharacterReader.termPush(field: Field<Nothing>): CharacterReader =
	copy(termOrNull = termOrNull.push(field))

val CharacterReader.termInvoke: CharacterReader
	get() =
		if (termOrNull == null) this
		else copy(termOrNull = invoke(termOrNull))

val CharacterReader.termParse: CharacterReader?
	get() =
		copy(termOrNull = null).orNull
			.fold(termOrNull?.fieldStreamOrNull) { field ->
				if (this == null) null
				else if (termOrNull != null) termPush(field)
				else if (field == leoWord fieldTo continueWord.term) this
				else field.leoReadCharacterOrNull.let { character ->
					if (character == null) termPush(field)
					else readPreprocessed(character)
				}
			}

fun CharacterReader.readPreprocessed(character: Character): CharacterReader? =
	characterEvaluator.evaluate(character)?.let { characterEvaluator ->
		copy(characterEvaluator = characterEvaluator)
	}

// === leo read bit

fun leoReadField(character: Character): Field<Nothing> =
	leoWord fieldTo term(readWord fieldTo character.reflect.term)

val Field<Nothing>.leoReadCharacterOrNull: Character?
	get() =
		get(leoWord)?.let { theLeoTerm ->
			theLeoTerm.value?.match(readWord) { readTerm ->
				readTerm?.match(characterWord) { characterTerm ->
					characterWord.fieldTo(characterTerm).parseCharacter
				}
			}
		}

// === bit stream

val CharacterReader.bitStreamOrNull: Stream<Bit>?
	get() =
		characterEvaluator.bitStreamOrNull

fun CharacterReader.invoke(term: Term<Nothing>) =
	characterEvaluator.invoke(term)