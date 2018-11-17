package leo

import leo.base.Bit
import leo.base.Stream

sealed class Character
data class LetterCharacter(val letter: Letter) : Character()
object BeginCharacter : Character()
object EndCharacter : Character()

val Character.bitStream: Stream<Bit>
	get() =
		when (this) {
			is LetterCharacter -> letter.bitStream
			is BeginCharacter -> beginBitStream
			is EndCharacter -> endBitStream
		}