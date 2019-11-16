package leo14.parser

import leo.base.ifOrNull

sealed class IndentParser
object BeginIndentParser : IndentParser()
object OneSpaceIndentParser : IndentParser()
object CompleteIndentParser : IndentParser()

val emptyIndentParser: IndentParser = BeginIndentParser

fun IndentParser.parse(char: Char): IndentParser? =
	ifOrNull(char == ' ') {
		when (this) {
			is BeginIndentParser -> OneSpaceIndentParser
			is OneSpaceIndentParser -> CompleteIndentParser
			is CompleteIndentParser -> null
		}
	}

val IndentParser.isComplete
	get() =
		this is CompleteIndentParser
