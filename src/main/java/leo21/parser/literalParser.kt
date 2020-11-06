package leo21.parser

import leo14.Literal
import leo14.literal

sealed class LiteralParser
data class StringLiteralParser(val stringParser: StringParser) : LiteralParser()
data class NumberLiteralParser(val numberParser: NumberParser) : LiteralParser()

val Char.beginLiteralParser: LiteralParser?
	get() =
		null
			?: beginStringParser?.let(::StringLiteralParser)
			?: beginNumberParser?.let(::NumberLiteralParser)

fun LiteralParser.plus(char: Char): LiteralParser? =
	when (this) {
		is StringLiteralParser -> stringParser.plus(char)?.let(::StringLiteralParser)
		is NumberLiteralParser -> numberParser.plus(char)?.let(::NumberLiteralParser)
	}

val LiteralParser.end: Literal?
	get() =
		when (this) {
			is StringLiteralParser -> stringParser.end?.let(::literal)
			is NumberLiteralParser -> numberParser.end?.let(::literal)
		}
