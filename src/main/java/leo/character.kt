package leo

import leo.base.*
import leo.binary.Int5
import leo.binary.incOrNull
import leo.binary.wrappingInt5

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

val Character.bitStream: Stream<EnumBit>
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

val Stream<EnumBit>.bitParseCharacter: Parse<EnumBit, Character>?
	get() = null
		?: bitParseLetter?.map { it.character }
		?: bitParseBegin?.map { it.character }
		?: bitParseEnd?.map { it.character }

val characterCount =
	letterCount + 2

val Character.int
	get() =
		when (this) {
			is LetterCharacter -> letter.int
			is BeginCharacter -> letterCount
			is EndCharacter -> letterCount + 1
		}

val Character.bitSequence
	get() =
		int.bitSequence(characterCount.bitCount)

val Int5.characterOrNull
	get() = null
		?: letterCharacterOrNull
		?: beginCharacterOrNull
		?: endCharacterOrNull

val Int5.letterCharacterOrNull
	get() =
		letterOrNull?.character

val Int5.beginCharacterOrNull
	get() =
		if (this == Letter.Z.int5.incOrNull) begin.character else null

val Int5.endCharacterOrNull
	get() =
		if (this == begin.character.int.wrappingInt5.incOrNull) end.character else null
