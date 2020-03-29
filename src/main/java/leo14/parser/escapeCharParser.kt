package leo14.parser

import leo.base.Empty

sealed class EscapeCharParser
data class EmptyEscapeCharParser(val empty: Empty) : EscapeCharParser()
data class CharEscapeCharParser(val char: Char) : EscapeCharParser()

val newlineEscapeChar: Char = 'n'
val tabEscapeChar: Char = 't'
val backslashEscapeChar: Char = '\\'
val doubleQuoteEscapeChar: Char = '"'

fun EscapeCharParser.parse(char: Char): EscapeCharParser? =
	when (this) {
		is EmptyEscapeCharParser ->
			when (char) {
				backslashEscapeChar -> CharEscapeCharParser('\\')
				newlineEscapeChar -> CharEscapeCharParser('\n')
				tabEscapeChar -> CharEscapeCharParser('\t')
				doubleQuoteEscapeChar -> CharEscapeCharParser('"')
				else -> null
			}
		is CharEscapeCharParser -> null
	}

val EscapeCharParser.charOrNull
	get() =
		when (this) {
			is EmptyEscapeCharParser -> null
			is CharEscapeCharParser -> char
		}

val Char.escapedString
	get() =
		when (this) {
			newlineEscapeChar -> "$escapeChar$newlineEscapeChar"
			backslashEscapeChar -> "$escapeChar$backslashEscapeChar"
			tabEscapeChar -> "$escapeChar$tabEscapeChar"
			doubleQuoteEscapeChar -> "$escapeChar$doubleQuoteEscapeChar"
			else -> toString()
		}