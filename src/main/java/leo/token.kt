package leo

import leo.base.*

sealed class Token<out V>
data class MetaEndToken<V>(val value: V) : Token<V>()
data class BeginToken<V>(val word: Word) : Token<V>()
data class EndToken<V>(val end: End) : Token<V>()

fun <V> Word.beginToken(): Token<V> =
	BeginToken(this)

fun <V> End.token(): Token<V> =
	EndToken(this)

val <V> V.metaEndToken: Token<V>
	get() =
		MetaEndToken(this)

val Word.beginToken: Token<Nothing>
	get() =
		beginToken()

val End.token: Token<Nothing>
	get() =
		EndToken(this)

val Token<Nothing>.reflect: Field<Nothing>
	get() =
		tokenWord fieldTo term(
			when (this) {
				is MetaEndToken -> fail
				is BeginToken -> beginWord fieldTo word.reflect.termOrNull
				is EndToken -> endWord.field
			})

val Field<Nothing>.parseToken: Token<Nothing>?
	get() =
		match(tokenWord) { tokenTerm ->
			when (tokenTerm) {
				endWord.term -> end.token
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
			is MetaEndToken -> fail
			is BeginToken -> word.letterStream.map(Letter::character).then { begin.character.onlyStream }
			is EndToken -> end.character.onlyStream
		}

val Token<Nothing>.bitStream: Stream<Bit>
	get() =
		characterStream.map(Character::bitStream).join

val Stream<Bit>.bitParseToken: Parse<Bit, Token<Nothing>>?
	get() = null
		?: bitParseWord?.bind { word ->
			bitParseBegin?.bind {
				parsed(word.beginToken)
			}
		}
		?: bitParseEnd?.bind { end ->
			parsed(end.token)
		}

