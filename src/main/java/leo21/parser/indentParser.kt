package leo21.parser

import leo.base.notNullIf
import leo.spaceChar

val indentSpaceCount = 2

data class IndentParser(val spaceCount: Int)

val Char.beginIndentParser: IndentParser?
	get() =
		notNullIf(indentSpaceCount > 1) { IndentParser(1) }

fun IndentParser.plus(char: Char): IndentParser? =
	notNullIf(char == spaceChar && spaceCount <= indentSpaceCount) {
		IndentParser(spaceCount.inc())
	}

val IndentParser.end: Indent?
	get() =
		notNullIf(spaceCount == indentSpaceCount) { indent }
