package leo14.parser

import leo.base.fold
import leo14.Literal
import leo14.literal

sealed class LiteralParser
object BeginLiteralParser : LiteralParser()
data class StringLiteralParser(val stringParser: StringParser) : LiteralParser()
data class NumberLiteralParser(val numberParser: NumberParser) : LiteralParser()

val newLiteralParser: LiteralParser = BeginLiteralParser
fun literalParser(stringParser: StringParser): LiteralParser = StringLiteralParser(stringParser)
fun literalParser(numberParser: NumberParser): LiteralParser = NumberLiteralParser(numberParser)

fun LiteralParser.parse(char: Char): LiteralParser? =
	when (this) {
		is BeginLiteralParser ->
			null
				?: emptyStringParser.parse(char)?.let(::literalParser)
				?: emptyNumberParser.parse(char)?.let(::literalParser)
		is StringLiteralParser ->
			stringParser.parse(char)?.let(::literalParser)
		is NumberLiteralParser ->
			numberParser.parse(char)?.let(::literalParser)
	}

val LiteralParser.literalOrNull: Literal?
	get() =
		when (this) {
			is BeginLiteralParser -> null
			is StringLiteralParser -> stringParser.stringOrNull?.let { literal(it) }
			is NumberLiteralParser -> numberParser.numberOrNull?.let { literal(it) }
		}

val LiteralParser.literal get() = literalOrNull!!

fun parseLiteral(string: String) =
	newLiteralParser.fold(string) { parse(it)!! }.literal

val LiteralParser.coreString: String
	get() =
		when (this) {
			is BeginLiteralParser -> ""
			is StringLiteralParser -> stringParser.coreString
			is NumberLiteralParser -> numberParser.coreString
		}

val LiteralParser.spacedString: String
	get() =
		coreString

val LiteralParser.canContinue
	get() =
		this is NumberLiteralParser