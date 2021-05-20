package leo25

import leo.base.charSeq
import leo.base.ifOrNull
import leo.base.notNullIf
import leo.base.orNullFold
import leo13.*

data class Parser<T>(
	val plusCharFn: (Char) -> Parser<T>?,
	val parsedOrNullFn: () -> T?
)

fun <T> Parser<T>.plus(char: Char) = plusCharFn(char)
val <T> Parser<T>.parsedOrNull: T? get() = parsedOrNullFn()
fun <T> parsedParser(parsed: T) = Parser({ null }, { parsed })
fun <T> partialParser(plusCharFn: (Char) -> Parser<T>?) = Parser(plusCharFn) { null }

fun <T> Parser<T>.parsed(string: String) =
	orNullFold(string.charSeq) { plus(it) }?.parsedOrNull

fun nameParser(charStack: Stack<Char>): Parser<String> = Parser(
	{ char -> notNullIf(char.isLetter()) { nameParser(charStack.push(char)) } },
	{ notNullIf(!charStack.isEmpty) { charStack.charString } }
)

val nameParser get() = nameParser(stack())

val escapeCharParser: Parser<Char>
	get() =
		partialParser { char ->
			when (char) {
				'\\' -> parsedParser('\\')
				'n' -> parsedParser('\n')
				't' -> parsedParser('\t')
				'"' -> parsedParser('"')
				else -> null
			}
		}

fun stringBodyParser(
	charStack: Stack<Char>,
	escapeCharParserOrNull: Parser<Char>?
): Parser<String> = Parser(
	{ char ->
		if (escapeCharParserOrNull != null)
			escapeCharParserOrNull.plus(char).let { escapeCharParser ->
				if (escapeCharParser != null) stringBodyParser(charStack, escapeCharParser)
				else escapeCharParserOrNull.parsedOrNull.let { escapedChar ->
					if (escapedChar != null) stringBodyParser(charStack.push(escapedChar), null).plus(char)
					else null
				}
			}
		else
			when (char) {
				'\\' -> stringBodyParser(charStack, escapeCharParser)
				'\"' -> null
				else -> stringBodyParser(charStack.push(char), null)
			}
	},
	{
		if (escapeCharParserOrNull == null) charStack.charString
		else escapeCharParserOrNull.parsedOrNull.let { escapedCharOrNull ->
			if (escapedCharOrNull == null) null
			else charStack.push(escapedCharOrNull).charString
		}
	}
)

fun stringParser(stringBodyParser: Parser<String>): Parser<String> =
	partialParser { char ->
		stringBodyParser.plus(char).let { newStringBodyParserOrNull ->
			if (newStringBodyParserOrNull != null) stringParser(newStringBodyParserOrNull)
			else ifOrNull(char == '"') {
				stringBodyParser.parsedOrNull?.let { parsedParser(it) }
			}
		}
	}

val stringParser: Parser<String>
	get() =
		partialParser { char ->
			notNullIf(char == '"') {
				stringParser(stringBodyParser(stack(), null))
			}
		}
