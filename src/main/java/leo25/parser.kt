package leo25

import leo.base.*
import leo13.*
import leo13.Stack
import leo14.*
import leo14.Number

data class Parser<T>(
	val plusCharFn: (Char) -> Parser<T>?,
	val parsedOrNullFn: () -> T?
)

fun <T> Parser<T>.plus(char: Char) = plusCharFn(char)
val <T> Parser<T>.parsedOrNull: T? get() = parsedOrNullFn()
fun <T> parsedParser(parsed: T) = Parser({ null }, { parsed })
fun <T> partialParser(plusCharFn: (Char) -> Parser<T>?) = Parser(plusCharFn) { null }

fun parser(string: String, startIndex: Int): Parser<String> =
	if (startIndex == string.length) parsedParser(string)
	else partialParser { char ->
		notNullIf(char == string[startIndex]) {
			parser(string, startIndex.inc())
		}
	}

fun parser(string: String): Parser<String> = parser(string, 0)

fun unitParser(char: Char): Parser<Unit> = parser(char).map { Unit }
fun unitParser(string: String): Parser<Unit> = parser(string).map { Unit }

fun charParser(fn: (Char) -> Boolean): Parser<Char> =
	partialParser { char ->
		notNullIf(fn(char)) {
			parsedParser(char)
		}
	}

val charParser: Parser<Char> get() = charParser { true }

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

val positiveNumberParser: Parser<Number>
	get() =
		digitCharParser.stackLinkParser.map {
			it.asStack.charString.numberOrNull
		}

val <T> Parser<T>.isPresentParser: Parser<Boolean>
	get() =
		map { true }.firstCharOr(parsedParser(false))

val numberParser: Parser<Number>
	get() =
		unitParser("-").isPresentParser.bind { negated ->
			positiveNumberParser.map { number ->
				number.runIf(negated) { unaryMinus() }
			}
		}

val literalParser: Parser<Literal>
	get() =
		stringParser.map { literal(it) }
			.firstCharOr(numberParser.map { literal(it) })

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

val escapeSequenceCharParser: Parser<Char>
	get() =
		unitParser('\\').bind { escapeCharParser }

val stringCharParser = charParser { it != '"' }

val stringBodyParser: Parser<String>
	get() =
		escapeSequenceCharParser
			.firstCharOr(stringCharParser)
			.stackParser
			.map { it.charString }

val stringParser: Parser<String>
	get() = stringBodyParser.enclosedWith(unitParser('"'))

val indentParser: Parser<Unit> get() = unitParser("  ")
