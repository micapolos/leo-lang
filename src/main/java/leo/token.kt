package leo

import leo.base.*

sealed class Token<out V>
data class MetaToken<V>(val meta: Meta<V>) : Token<V>()
data class WordToken<V>(val word: Word) : Token<V>()
data class ControlToken<V>(val control: Control) : Token<V>()

val <V> Meta<V>.token: Token<V>
	get() =
		MetaToken(this)

fun <V> Word.token(): Token<V> =
	WordToken(this)

val Word.token: Token<Nothing>
	get() =
		token()

fun <V> Control.token(): Token<V> =
	ControlToken(this)

val Control.token: Token<Nothing>
	get() =
		token()

val Token<Nothing>.reflect: Field<Nothing>
	get() =
		tokenWord fieldTo term(
			when (this) {
				is MetaToken -> fail
				is WordToken -> word.reflect
				is ControlToken -> control.reflect
			})

val Field<Nothing>.parseToken: Token<Nothing>?
	get() =
		matchKey(tokenWord) {
			onlyFieldOrNull?.matchKey(wordWord) {
				matchWord {
					token
				}
			} ?: onlyFieldOrNull?.parseControl?.token
		}

// === streams

val Token<Nothing>.characterStream: Stream<Character>
	get() =
		when (this) {
			is MetaToken -> fail
			is WordToken -> word.letterStream.map(Letter::character)
			is ControlToken -> control.character.onlyStream
		}

val Token<Nothing>.bitStream: Stream<Bit>
	get() =
		characterStream.mapJoin(Character::bitStream)

val Stream<Bit>.bitParseToken: Parse<Bit, Token<Nothing>>?
	get() = null
		?: bitParseWord.bind { word ->
			parsed(word.token)
		} ?: bitParseBegin.bind { begin ->
			parsed(begin.control.token)
		} ?: bitParseEnd.bind { end ->
			parsed(end.control.token)
		}

