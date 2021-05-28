package leo25.parser

import leo.base.charSeq
import leo.base.fold
import leo25.throwError
import leo25.value

data class ParserAndLocation<T>(
	val location: Location,
	val parser: Parser<T>
)

fun <T> ParserAndLocation<T>.plus(char: Char): ParserAndLocation<T> =
	ParserAndLocation(
		if (char == '\n') location.newLine else location.nextColumn,
		parser.plus(char) ?: throwParserError()
	)

fun <T> ParserAndLocation<T>.plus(string: String): ParserAndLocation<T> =
	fold(string.charSeq) { plus(it) }

fun <T, O> ParserAndLocation<T>.throwParserError(): O =
	location.script.value.throwError()

fun <T> Parser<T>.parseOrThrow(string: String): T =
	ParserAndLocation(startLocation, this)
		.plus(string)
		.run { parser.parsedOrNull ?: throwParserError() }

