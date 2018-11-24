package leo

import leo.base.*

sealed class Character

data class BeginCharacter(val begin: Begin) : Character() {
	override fun toString() = reflect.string
}

data class EndCharacter(val end: End) : Character() {
	override fun toString() = reflect.string
}

data class LetterCharacter(val letter: Letter) : Character() {
	override fun toString() = reflect.string
}

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

val Character.bitStream: Stream<Bit>
	get() =
		byte.bitStream

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
				is LetterCharacter -> letter.reflect.value
			}

val Field<Nothing>.parseCharacter: Character?
	get() =
		matchKey(characterWord) {
			matchWord(beginWord) { begin.character } ?: matchWord(endWord) { end.character }
			?: (letterWord fieldTo this).parseLetter?.character
		}

val Stream<Bit>.bitParseCharacter: Parse<Bit, Character>?
	get() = null
		?: bitParseLetter?.map { it.character }
		?: bitParseBegin?.map { it.character }
		?: bitParseEnd?.map { it.character }
