package leo

import leo.base.Bit
import leo.base.Stream

sealed class Character
object BeginCharacter : Character()
object EndCharacter : Character()
data class LetterCharacter(val letter: Letter) : Character()

val Character.bitStream: Stream<Bit>
	get() =
		when (this) {
			is LetterCharacter -> letter.bitStream
			is BeginCharacter -> beginBitStream
			is EndCharacter -> endBitStream
		}

val Byte.characterOrNull: Character?
	get() =
		when (toChar()) {
			'(' -> BeginCharacter
			')' -> EndCharacter
			else -> letterOrNull?.let { LetterCharacter(it) }
		}

val Character.reflect: Field<Nothing>
	get() =
		characterWord fieldTo
			when (this) {
				is BeginCharacter -> beginWord.term
				is EndCharacter -> endWord.term
				is LetterCharacter -> letter.reflect.termOrNull
			}

val Field<Nothing>.parseCharacter: Character?
	get() =
		match(characterWord) { characterTerm ->
			when (characterTerm) {
				beginWord.term -> BeginCharacter
				endWord.term -> EndCharacter
				else -> letterWord.fieldTo(characterTerm).parseLetter?.let { letter ->
					LetterCharacter(letter)
				}
			}
		}
