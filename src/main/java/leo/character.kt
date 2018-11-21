package leo

import leo.base.Bit
import leo.base.Parse
import leo.base.Stream
import leo.base.map

sealed class Character
data class BeginCharacter(val begin: Begin) : Character()
data class EndCharacter(val end: End) : Character()
data class LetterCharacter(val letter: Letter) : Character()

val Begin.character: Character
	get() =
		BeginCharacter(this)

val End.character: Character
	get() =
		EndCharacter(this)

val Letter.character: Character
	get() =
		LetterCharacter(this)

val Character.byte: Byte
	get() =
		when (this) {
			is LetterCharacter -> letter.byte
			is BeginCharacter -> beginByte
			is EndCharacter -> endByte
		}

val Character.char: Char
	get() =
		byte.toChar()

val Byte.characterOrNull: Character?
	get() =
		when (this) {
			beginByte -> begin.character
			endByte -> end.character
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
				beginWord.term -> BeginCharacter(Begin)
				endWord.term -> EndCharacter(End)
				else -> letterWord.fieldTo(characterTerm).parseLetter?.let { letter ->
					LetterCharacter(letter)
				}
			}
		}

val Stream<Bit>.bitParseCharacter: Parse<Bit, Character>?
	get() = null
		?: bitParseLetter?.map { it.character }
		?: bitParseBegin?.map { it.character }
		?: bitParseEnd?.map { it.character }
