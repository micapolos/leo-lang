package leo14.parser

import leo.base.fold
import leo14.*

sealed class LiteralParser
object EmptyLiteralParser : LiteralParser()
data class StringLiteralParser(val stringParser: StringParser) : LiteralParser()
data class NumberLiteralParser(val numberParser: NumberParser) : LiteralParser()

val newLiteralParser: LiteralParser = EmptyLiteralParser
fun literalParser(stringParser: StringParser): LiteralParser = StringLiteralParser(stringParser)
fun literalParser(numberParser: NumberParser): LiteralParser = NumberLiteralParser(numberParser)

fun LiteralParser.parse(char: Char): LiteralParser? =
	when (this) {
		is EmptyLiteralParser ->
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
			is EmptyLiteralParser -> null
			is StringLiteralParser -> stringParser.stringOrNull?.let { literal(it) }
			is NumberLiteralParser -> numberParser.numberOrNull?.let { literal(it) }
		}

val LiteralParser.literal get() = literalOrNull!!

fun parseLiteral(string: String) =
	newLiteralParser.fold(string) { parse(it)!! }.literal

val LiteralParser.coreString: String
	get() =
		when (this) {
			is EmptyLiteralParser -> ""
			is StringLiteralParser -> stringParser.coreString
			is NumberLiteralParser -> numberParser.coreString
		}

val LiteralParser.spacedString: String
	get() =
		coreString

val LiteralParser.canContinue
	get() =
		this is NumberLiteralParser

val LiteralParser.reflectScriptLine
	get() =
		"literal" lineTo script(
			"parser" lineTo script(
				when (this) {
					EmptyLiteralParser -> "empty".line
					is StringLiteralParser -> stringParser.reflectScriptLine
					is NumberLiteralParser -> numberParser.reflectScriptLine
				}))