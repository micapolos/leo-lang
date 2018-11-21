package leo

import leo.base.*

data class CharacterReader(
	val characterEvaluator: CharacterEvaluator,
	val termOrNull: Term<Nothing>?)

val emptyCharacterReader =
	CharacterReader(emptyCharacterEvaluator, null)

fun CharacterReader.read(character: Character): CharacterReader? =
	if (!readersEnabled || !byteReaderEnabled) readPreprocessed(character)
	else this
		.termPush(character.reflect)
		.termInvoke(character)

fun CharacterReader.termPush(field: Field<Nothing>): CharacterReader =
	copy(termOrNull = termOrNull.push(field))

fun CharacterReader.termInvoke(character: Character): CharacterReader? =
	if (termOrNull == null) this
	else termOrNull
		.push(leoWord fieldTo readWord.term)
		.let { argument ->
			function
				.get(argument)
				.let { matchOrNull ->
					if (matchOrNull == null)
						copy(termOrNull = null).readPreprocessed(character)
					else when (matchOrNull.bodyBinaryTrieMatch) {
						is BinaryTrie.Match.Partial ->
							this // partial - continue
						is BinaryTrie.Match.Full ->
							copy(termOrNull = matchOrNull.bodyBinaryTrieMatch.value.apply(argument)).termParse
					}
				}
		}

val CharacterReader.termParse: CharacterReader?
	get() =
		copy(termOrNull = null).orNull
			.fold(termOrNull?.fieldStreamOrNull?.reverse) { field ->
				if (this == null) null
				else if (termOrNull != null) termPush(field)
				else field.parseCharacter.let { characterOrNull ->
					if (characterOrNull == null) termPush(field)
					else readPreprocessed(characterOrNull)
				}
			}

fun CharacterReader.readPreprocessed(character: Character): CharacterReader? =
	characterEvaluator.evaluate(character)?.let { characterEvaluator ->
		copy(characterEvaluator = characterEvaluator)
	}

// === bit stream

val CharacterReader.bitStreamOrNull: Stream<Bit>?
	get() =
		characterEvaluator.bitStreamOrNull

val CharacterReader.function: Function
	get() =
		characterEvaluator.function
