package leo7

import leo.base.ifNull

sealed class TokenParser

data class PartialTokenParser(val wordParser: WordParser) : TokenParser()
data class FullTokenParser(val token: Token) : TokenParser()

val WordParser.tokenParser: TokenParser get() = PartialTokenParser(this)
val Token.tokenParser: TokenParser get() = FullTokenParser(this)
val newTokenParser get() = newWordParser.tokenParser
val newPartialTokenParser get() = PartialTokenParser(newWordParser)

fun TokenParser.read(char: Char): TokenParser? = when (this) {
	is FullTokenParser -> null
	is PartialTokenParser -> when (char) {
		'(' -> wordParser.parsedWordOrNull?.begin?.token?.tokenParser
		')' -> wordParser.parsedWordOrNull.ifNull { end.token.tokenParser }
		else -> wordParser.read(char)?.tokenParser
	}
}

val TokenParser.parsedTokenOrNull
	get() = when (this) {
		is PartialTokenParser -> null
		is FullTokenParser -> token
	}
