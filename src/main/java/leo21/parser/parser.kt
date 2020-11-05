package leo21.parser

import leo.base.charSeq
import leo.base.notNullIf
import leo.base.orNullFold
import leo13.Either
import leo13.Stack
import leo13.array
import leo13.firstEither
import leo13.push
import leo13.secondEither
import leo13.stack

data class Parser<out V : Any>(
	val parsedOrNullFn: () -> V?,
	val plusFn: (Char) -> Parser<V>?
)

val <V : Any> Parser<V>.parsedOrNull: V? get() = parsedOrNullFn()
fun <V : Any> Parser<V>.plus(char: Char): Parser<V>? = plusFn(char)

fun <V : Any> parser(parsedOrNullFn: () -> V?, plusFn: (Char) -> Parser<V>?): Parser<V> =
	Parser(parsedOrNullFn, plusFn)

fun <V : Any> parser(plusFn: (Char) -> Parser<V>?): Parser<V> =
	parser({ null }, plusFn)

fun <V : Any> Parser<V>.plus(string: String): Parser<V>? =
	orNullFold(string.charSeq) { plus(it) }

fun <V : Any> Parser<V>.parse(string: String): V? =
	plus(string)?.parsedOrNull

fun <V : Any> Parser<V>.failIf(fn: (V) -> Boolean): Parser<V> =
	parser(parsedOrNullFn) { char ->
		plus(char)?.let { parser ->
			parser.parsedOrNull
				.let { parsedOrNull ->
					notNullIf(parsedOrNull == null || !fn(parsedOrNull)) {
						parser.failIf(fn)
					}
				}
		}
	}

val char = parser { char -> char.completedParser }
val stringParser: Parser<String> = char.stackParser.map { String(it.array.toCharArray()) }
fun parser(char: Char): Parser<Char> = parser { notNullIf(it == char) { char.completedParser } }

fun <V : Any> Stack<V>.parser(itemParser: Parser<V>, newItemParser: Parser<V>, isComplete: Boolean): Parser<Stack<V>> =
	parser({ notNullIf(isComplete) { this } }) { char ->
		itemParser
			.plus(char)
			?.let { itemParser ->
				itemParser.parsedOrNull
					?.let { parsed -> push(parsed).parser(newItemParser, newItemParser, true) }
					?: parser(itemParser, newItemParser, false)
			}
	}

val <V : Any> Parser<V>.stackParser: Parser<Stack<V>>
	get() =
		stack<V>().parser(itemParser = this, newItemParser = this, isComplete = true)

val <V : Any> V.completedParser: Parser<V> get() = parser({ this }, { null })

fun <V : Any, R : Any> Parser<V>.map(fn: (V) -> R?): Parser<R> =
	parser(
		{ parsedOrNull?.let(fn) },
		{ char -> plus(char)?.map(fn) })

fun <V1 : Any, V2 : Any> Parser<V1>.to(parser: Parser<V2>): Parser<Pair<V1, V2>> =
	parsedOrNull.let { parsed ->
		if (parsed != null) parser.map { parsed to it }
		else parser { char -> plus(char)?.to(parser) }
	}

fun <V1 : Any, V2 : Any> Parser<V1>.or(parser: Parser<V2>): Parser<Either<V1, V2>> =
	parsedOrNull.let { parsed ->
		if (parsed != null) parsed.firstEither<V1, V2>().completedParser
		else parser { char ->
			plus(char)
				?.let { firstParser -> firstParser.map { it.firstEither<V1, V2>() } }
				?: parser.plus(char)?.map { it.secondEither<V1, V2>() }
		}
	}

fun <V : Any> Parser<V>.until(fn: (V) -> Boolean): Parser<V> =
	parsedOrNull.let { parsed ->
		if (parsed != null && fn(parsed)) parsed.completedParser
		else parser { char -> plus(char)?.until(fn) }
	}
