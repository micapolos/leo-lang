package leo

import leo.base.Stream
import leo.base.map
import leo.base.onlyStream
import leo.base.then

sealed class Token
data class BeginToken(val word: Word) : Token()
object EndToken : Token()

val Word.beginToken: Token
	get() =
		BeginToken(this)

val endToken: Token
	get() =
		EndToken

val Token.reflect: Field<Nothing>
	get() =
		tokenWord fieldTo term(
			when (this) {
				is BeginToken -> beginWord fieldTo word.reflect.termOrNull
				is EndToken -> endWord.field
			})

val Field<Nothing>.parseToken: Token?
	get() =
		match(tokenWord) { tokenTerm ->
			when (tokenTerm) {
				endWord.term -> EndToken
				else -> tokenTerm?.match(beginWord) { beginTerm ->
					beginTerm?.onlyFieldOrNull?.let { field ->
						if (field.termOrNull != null) null
						else BeginToken(field.word)
					}
				}
			}
		}

// === streams

val Token.characterStream: Stream<Character>
	get() =
		when (this) {
			is BeginToken -> word.letterStream.map(Letter::character).then { beginCharacter.onlyStream }
			is EndToken -> endCharacter.onlyStream
		}