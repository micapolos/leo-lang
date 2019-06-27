package leo7

import leo.base.ifNull

sealed class Token

data class BeginToken(val begin: WordBegin) : Token()
data class EndToken(val end: End) : Token()

val WordBegin.token: Token get() = BeginToken(this)
val End.token: Token get() = EndToken(this)

fun Writer<Token>.characterWriter(wordOrNull: Word?): Writer<Character> =
	writer { character ->
		when (character) {
			is LetterCharacter -> characterWriter(wordOrNull?.plus(character.letter) ?: character.letter.word)
			is LeftParenthesisCharacter -> wordOrNull?.let { word -> write(word.begin.token) }?.characterWriter(null)
			is RightParenthesisCharacter -> wordOrNull.ifNull { write(end.token) }?.characterWriter(null)
		}
	}

val Writer<Token>.characterWriter: Writer<Character> get() = characterWriter(null)