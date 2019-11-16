package leo14.parser

import leo.base.fold
import leo.base.notNullIf
import leo14.Token
import leo14.begin
import leo14.end
import leo14.token

sealed class TokenParser
data class NameTokenParser(val nameParser: NameParser) : TokenParser()
data class EndTokenParser(val token: Token) : TokenParser()

val tokenParser: TokenParser = NameTokenParser(nameParser)

fun TokenParser.parse(char: Char): TokenParser? =
	when (this) {
		is NameTokenParser ->
			when (char) {
				')' -> notNullIf(nameParser.isEmpty) { EndTokenParser(token(end)) }
				'(' -> nameParser.nameOrNull?.let(::begin)?.let { token(it) }?.let(::EndTokenParser)
				else -> nameParser.parse(char)?.let(::NameTokenParser)
			}
		is EndTokenParser ->
			null
	}

val TokenParser.token
	get() =
		(this as EndTokenParser).token

fun parseToken(string: String): Token =
	tokenParser.fold(string) { parse(it)!! }.token