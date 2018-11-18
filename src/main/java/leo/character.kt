package leo

import leo.base.Bit
import leo.base.Stream

sealed class Character
object BeginCharacter : Character()
object EndCharacter : Character()
data class LetterCharacter(val letter: Letter) : Character()

val Letter.character: Character
	get() =
		LetterCharacter(this)

val beginCharacter: Character =
	BeginCharacter

val endCharacter: Character =
	EndCharacter

val Character.byte: Byte
	get() =
		when (this) {
			is LetterCharacter -> letter.byte
			is BeginCharacter -> beginByte
			is EndCharacter -> endByte
		}

val Character.bitStream: Stream<Bit>
	get() =
		when (this) {
			is LetterCharacter -> letter.bitStream
			is BeginCharacter -> beginBitStream
			is EndCharacter -> endBitStream
		}

val Byte.characterOrNull: Character?
	get() =
		when (this) {
			beginByte -> BeginCharacter
			endByte -> EndCharacter
			else -> letterOrNull?.character
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
