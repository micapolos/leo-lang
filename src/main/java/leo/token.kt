package leo

import leo.base.*

sealed class Token<out V>
data class MetaToken<V>(val value: V) : Token<V>()
data class WordToken<V>(val word: Word) : Token<V>()
data class BeginToken<V>(val begin: Begin) : Token<V>()
data class EndToken<V>(val end: End) : Token<V>()

val <V> V.metaToken: Token<V>
	get() =
		MetaToken(this)

fun <V> Word.token(): Token<V> =
	WordToken(this)

val Word.token: Token<Nothing>
	get() =
		token()

fun <V> Begin.token(): Token<V> =
	BeginToken(this)

val Begin.token: Token<Nothing>
	get() =
		token()

fun <V> End.token(): Token<V> =
	EndToken(this)

val End.token: Token<Nothing>
	get() =
		token()

val Token<Nothing>.reflect: Field<Nothing>
	get() =
		tokenWord fieldTo
			when (this) {
				is MetaToken -> fail
				is WordToken -> word.reflect.onlyTerm
				is BeginToken -> beginWord.term
				is EndToken -> endWord.term
			}

val Field<Nothing>.parseToken: Token<Nothing>?
	get() =
		matchKey(tokenWord) {
			onlyFieldOrNull?.matchKey(wordWord) {
				matchWord {
					token
				}
			} ?: matchWord(beginWord) {
				begin.token
			} ?: matchWord(endWord) {
				end.token
			}
		}

// === streams

val Token<Nothing>.characterStream: Stream<Character>
	get() =
		when (this) {
			is MetaToken -> fail
			is WordToken -> word.letterStream.map(Letter::character)
			is BeginToken -> begin.character.onlyStream
			is EndToken -> end.character.onlyStream
		}

val Token<Nothing>.bitStream: Stream<Bit>
	get() =
		characterStream.mapJoin(Character::bitStream)

val Stream<Bit>.bitParseToken: Parse<Bit, Token<Nothing>>?
	get() = null
		?: bitParseWord.bind { word ->
			parsed(word.token)
		} ?: bitParseBegin.bind { begin ->
			parsed(begin.token)
		} ?: bitParseEnd.bind { end ->
			parsed(end.token)
		}

