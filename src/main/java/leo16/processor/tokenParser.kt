package leo16.processor

import leo.base.nullOf
import leo13.Stack
import leo13.onlyStack
import leo14.Token
import leo14.begin
import leo14.token
import leo16.base.emptyText

sealed class TokenParser
data class TextParserTokenParser(val textParser: TextParser) : TokenParser()
data class WordTokenParser(val word: Word) : TokenParser()

sealed class TokenParsed
data class ParserTokenParsed(val tokenParser: TokenParser) : TokenParsed()
data class TokenStackTokenParsed(val tokenStack: Stack<Token>) : TokenParsed()

val Char.tokenParserOrNull: TokenParser?
	get() =
		when (this) {
			'"' -> TextParserTokenParser(TextParser(emptyText, false))
			else -> nullOf<Word>().plusOrNull(this)?.let { WordTokenParser(it) }
		}

fun TokenParser.parse(char: Char): TokenParsed? =
	when (this) {
		is TextParserTokenParser ->
			textParser.parse(char)?.let { tokenParsed ->
				when (tokenParsed) {
					is TextParserStringParser -> ParserTokenParsed(TextParserTokenParser(tokenParsed.textParser))
					is StringStringParser -> TokenStackTokenParsed(token(begin(tokenParsed.string)).onlyStack)
				}
			}
		is WordTokenParser -> when (char) {
			' ' -> TODO()
			else -> word.plusOrNull(char)?.let { word ->
				ParserTokenParsed(WordTokenParser(word))
			}
		}
	}
