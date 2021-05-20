package leo25

import leo.base.*
import leo.base.reverse
import leo13.*
import leo13.Stack
import leo13.charString
import leo13.push
import leo13.seq
import leo25.pushParser

data class Parser<T>(
	val plusCharFn: (Char) -> Parser<T>?,
	val parsedOrNullFn: () -> T?
)

fun <T> Parser<T>.plus(char: Char) = plusCharFn(char)
val <T> Parser<T>.parsedOrNull: T? get() = parsedOrNullFn()
fun <T> parsedParser(parsed: T) = Parser({ null }, { parsed })
fun <T> partialParser(plusCharFn: (Char) -> Parser<T>?) = Parser(plusCharFn) { null }

fun parser(string: String, index: Int): Parser<String> =
	if (index == string.length) parsedParser(string)
	else partialParser { char ->
		notNullIf(char == string[index]) {
			parser(string, index.inc())
		}
	}

fun parser(string: String): Parser<String> = parser(string, 0)

fun charParser(fn: (Char) -> Boolean): Parser<Char> =
	partialParser { char ->
		notNullIf(fn(char)) {
			parsedParser(char)
		}
	}

fun parser(char: Char): Parser<Char> = charParser { it == char }
val letterCharParser: Parser<Char> get() = charParser { it.isLetter() }
val digitCharParser: Parser<Char> get() = charParser { it.isDigit() }

fun <T> Parser<T>.enclosedWith(left: Parser<*>, right: Parser<*> = left): Parser<T> =
	left.bind {
		bind { string ->
			right.bind {
				parsedParser(string)
			}
		}
	}

fun <T> parser(stack: Stack<T>, parser: Parser<T>): Parser<Stack<T>> =
	Parser(
		{ char -> parser.plus(char)?.let { parser(stack, parser, it) } },
		{ stack }
	)

fun <T> parser(stack: Stack<T>, parser: Parser<T>, partialParser: Parser<T>): Parser<Stack<T>> =
	Parser(
		{ char ->
			partialParser.plus(char).let { newPartialParserOrNull ->
				if (newPartialParserOrNull != null) parser(stack, parser, newPartialParserOrNull)
				else partialParser.parsedOrNull?.let { parser(stack.push(it), parser).plus(char) }
			}
		},
		{ partialParser.parsedOrNull?.let { stack.push(it) } }
	)

fun <T> stackParser(parser: Parser<T>): Parser<Stack<T>> =
	parser(stack(), parser)

fun <T> Stack<T>.pushParser(parser: Parser<T>): Parser<Stack<T>> =
	parser(this, parser)

val <T> Parser<T>.stackParser: Parser<Stack<T>>
	get() =
		stack<T>().pushParser(this)

val <T> Parser<T>.stackLinkParser: Parser<StackLink<T>>
	get() =
		stackParser.map { it.linkOrNull }

fun <T, O> Parser<T>.map(fn: (T) -> O?): Parser<O> =
	Parser(
		{ char -> plus(char)?.map(fn) },
		{ parsedOrNull?.let { fn(it) } }
	)

fun <T, O> Parser<T>.bind(fn: (T) -> Parser<O>): Parser<O> =
	Parser(
		{ char ->
			plus(char).let { newParserOrNull ->
				if (newParserOrNull != null) newParserOrNull.bind(fn)
				else parsedOrNull?.let { fn(it).plus(char) }
			}
		},
		{ parsedOrNull?.let { fn(it).parsedOrNull } })

fun <T> Parser<T>.firstCharOr(parser: Parser<T>): Parser<T> =
	Parser(
		{ char ->
			plus(char).let { newParserOrNull ->
				if (newParserOrNull != null) newParserOrNull
				else parser.plus(char)
			}
		},
		{ parsedOrNull ?: parser.parsedOrNull }
	)

fun <T> Parser<T>.or(parser: Parser<T>, charStack: Stack<Char>): Parser<T> =
	Parser(
		{ char ->
			plus(char).let { newParserOrNull ->
				if (newParserOrNull != null) newParserOrNull.or(parser, charStack.push(char))
				else parser.orNullFold(charStack.seq.reverse) { plus(it) }?.plus(char)
			}
		},
		{ parsedOrNull ?: parser.parsedOrNull }
	)

fun <T> Parser<T>.or(parser: Parser<T>): Parser<T> =
	or(parser, stack())

fun <T> Parser<T>.parsed(string: String) =
	orNullFold(string.charSeq) { plus(it) }?.parsedOrNull

val nameParser: Parser<String>
	get() =
		letterCharParser.stackLinkParser.map {
			it.asStack.charString
		}

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

val stringBodyParser: Parser<String> get() = stringBodyParser(stack(), null)

val stringParser: Parser<String>
	get() = stringBodyParser.enclosedWith(parser('"'))

val indentParser: Parser<Unit> get() = parser("  ").map { Unit }
