package leo14.parser

import leo.base.fold
import leo14.literal

sealed class LiteralParser
object BeginLiteralParser : LiteralParser()
data class StringLiteralParser(val stringParser: StringParser) : LiteralParser()
data class IntLiteralParser(val intParser: IntParser) : LiteralParser()

val literalParser: LiteralParser = BeginLiteralParser
fun literalParser(stringParser: StringParser): LiteralParser = StringLiteralParser(stringParser)
fun literalParser(intParser: IntParser): LiteralParser = IntLiteralParser(intParser)

fun LiteralParser.parse(char: Char): LiteralParser? =
	when (this) {
		is BeginLiteralParser ->
			null
				?: stringParser.parse(char)?.let(::literalParser)
				?: intParser.parse(char)?.let(::literalParser)
		is StringLiteralParser ->
			stringParser.parse(char)?.let(::literalParser)
		is IntLiteralParser ->
			intParser.parse(char)?.let(::literalParser)
	}

val LiteralParser.literal
	get() =
		when (this) {
			is BeginLiteralParser -> null
			is StringLiteralParser -> literal(stringParser.string)
			is IntLiteralParser -> literal(intParser.int)
		}!!

fun parseLiteral(string: String) =
	literalParser.fold(string) { parse(it)!! }.literal