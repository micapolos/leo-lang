package leo25.parser

import leo.base.charSeq
import leo.base.fold
import leo14.literal
import leo25.field
import leo25.fieldTo
import leo25.throwError
import leo25.value

data class Parsing<T>(
	val lineNumber: Int,
	val columNumber: Int,
	val parser: Parser<T>
)

fun <T> Parsing<T>.plus(char: Char): Parsing<T> =
	Parsing(
		if (char == '\n') lineNumber.inc() else lineNumber,
		if (char == '\n') 0 else columNumber.inc(),
		parser.plus(char) ?: throwParserError()
	)

fun <T> Parsing<T>.plus(string: String): Parsing<T> =
	fold(string.charSeq) { plus(it) }

fun <T, O> Parsing<T>.throwParserError(): O =
	value(
		"line" fieldTo value(field(literal(lineNumber.inc()))),
		"column" fieldTo value(field(literal(columNumber.inc())))
	).throwError()

fun <T> Parser<T>.parseOrThrow(string: String): T =
	Parsing(0, 0, this).plus(string).let { parsing ->
		parsing.parser.parsedOrNull ?: parsing.throwParserError()
	}

