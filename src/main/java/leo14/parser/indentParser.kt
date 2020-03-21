package leo14.parser

import leo.base.ifOrNull

sealed class IndentParser
object EmptyIndentParser : IndentParser()
object OneSpaceIndentParser : IndentParser()
object CompleteIndentParser : IndentParser()

val emptyIndentParser: IndentParser = EmptyIndentParser

fun IndentParser.parse(char: Char): IndentParser? =
	ifOrNull(char == ' ') {
		when (this) {
			is EmptyIndentParser -> OneSpaceIndentParser
			is OneSpaceIndentParser -> CompleteIndentParser
			is CompleteIndentParser -> null
		}
	}

val IndentParser.isComplete
	get() =
		this is CompleteIndentParser
