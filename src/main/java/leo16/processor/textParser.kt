package leo16.processor

import leo.base.appendableString
import leo.base.runIfNotNull
import leo13.fold
import leo13.reverse
import leo16.base.Text
import leo16.base.plus

data class TextParser(val text: Text, val isEscaping: Boolean)

fun TextParser.parse(char: Char): StringParser? =
	if (isEscaping)
		runIfNotNull(char.escapedCharOrNull) { escapedChar ->
			TextParserStringParser(TextParser(text.plus(escapedChar), false))
		}
	else when (char) {
		escapeChar -> TextParserStringParser(TextParser(text, true))
		'"' -> StringStringParser(appendableString { it.fold(text.charStack.reverse) { append(it) } })
		else -> TextParserStringParser(TextParser(text.plus(char), false))
	}
