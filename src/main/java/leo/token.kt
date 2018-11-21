package leo

import leo.base.*

sealed class Token<out V> {
	data class MetaEnd<V>(val value: V) : Token<V>()
	data class Begin<V>(val word: Word) : Token<V>()
	data class End<V>(val unit: Unit) : Token<V>()
}

fun <V> Word.beginToken(): Token<V> =
	Token.Begin(this)

fun <V> endToken(): Token<V> =
	Token.End(Unit)

val <V> V.metaEndToken: Token<V>
	get() =
		Token.MetaEnd(this)

val Word.beginToken: Token<Nothing>
	get() =
		beginToken()

val endToken: Token<Nothing> =
	endToken()

val Token<Nothing>.reflect: Field<Nothing>
	get() =
		tokenWord fieldTo term(
			when (this) {
				is Token.MetaEnd -> fail
				is Token.Begin -> beginWord fieldTo word.reflect.termOrNull
				is Token.End -> endWord.field
			})

val Field<Nothing>.parseToken: Token<Nothing>?
	get() =
		match(tokenWord) { tokenTerm ->
			when (tokenTerm) {
				endWord.term -> endToken
				else -> tokenTerm?.match(beginWord) { beginTerm ->
					beginTerm?.onlyFieldOrNull?.let { field ->
						if (field.termOrNull != null) null
						else field.word.beginToken
					}
				}
			}
		}

// === streams

val Token<Nothing>.characterStream: Stream<Character>
	get() =
		when (this) {
			is Token.MetaEnd -> fail
			is Token.Begin -> word.letterStream.map(Letter::character).then { begin.character.onlyStream }
			is Token.End -> end.character.onlyStream
		}
