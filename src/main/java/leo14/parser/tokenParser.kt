package leo14.parser

import leo.base.fold
import leo.base.notNullIf
import leo14.Token
import leo14.begin
import leo14.end
import leo14.token

sealed class TokenParser
object BeginTokenParser : TokenParser()
data class NameTokenParser(val nameParser: NameParser) : TokenParser()
data class EndTokenParser(val token: Token) : TokenParser()
data class LiteralTokenParser(val literalParser: LiteralParser) : TokenParser()

val tokenParser: TokenParser = BeginTokenParser

fun TokenParser.parse(char: Char): TokenParser? =
	when (this) {
		is BeginTokenParser ->
			null
				?: nameParser.parse(char)?.let(::NameTokenParser)
				?: literalParser.parse(char)?.let(::LiteralTokenParser)
				?: notNullIf(char == ')') { EndTokenParser(token(end)) }
		is NameTokenParser ->
			when (char) {
				'(' -> nameParser.nameOrNull?.let(::begin)?.let { token(it) }?.let(::EndTokenParser)
				else -> nameParser.parse(char)?.let(::NameTokenParser)
			}
		is EndTokenParser ->
			null
		is LiteralTokenParser ->
			literalParser.parse(char)?.let(::LiteralTokenParser)
	}

val TokenParser.tokenOrNull: Token?
	get() =
		when (this) {
			is BeginTokenParser -> null
			is NameTokenParser -> null
			is LiteralTokenParser -> literalParser.literalOrNull?.let { token(it) }
			is EndTokenParser -> token
		}

val TokenParser.token
	get() =
		tokenOrNull!!

fun parseToken(string: String): Token =
	tokenParser.fold(string) { parse(it)!! }.token

val TokenParser.isEmpty get() = this is BeginTokenParser