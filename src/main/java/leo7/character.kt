package leo7

sealed class Character

data class LetterCharacter(val letter: Letter) : Character()
data class LeftParenthesisCharacter(val leftParenthesis: LeftParenthesis) : Character()
data class RightParenthesisCharacter(val rightParenthesis: RightParenthesis) : Character()

val Letter.character: Character get() = LetterCharacter(this)
val LeftParenthesis.character: Character get() = LeftParenthesisCharacter(this)
val RightParenthesis.character: Character get() = RightParenthesisCharacter(this)

val Char.characterOrNull
	get() = when (this) {
		'(' -> leftParenthesis.character
		')' -> rightParenthesis.character
		else -> letterOrNull?.character
	}

val Writer<Character>.charWriter: Writer<Char>
	get() =
		writer { char ->
			char.characterOrNull?.let { character ->
				write(character)?.charWriter
			}
		}
