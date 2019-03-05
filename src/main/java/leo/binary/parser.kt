package leo.binary

import leo.base.orNull

sealed class Parser<T>

data class PartialParser<F, T>(
	val input: F,
	val parseFn: F.(Bit) -> Parser<T>?) : Parser<T>()

data class DoneParser<T>(
	val parsed: T) : Parser<T>()

val <T> T.parser
	get() =
		DoneParser(this)

fun <F, T> Parser<F>.bind(fn: F.(Bit) -> Parser<T>?): Parser<T> =
	when (this) {
		is DoneParser -> PartialParser(parsed) { parsed.fn(it) }
		is PartialParser<*, F> -> PartialParser(input) { this@bind.parse(it)?.bind(fn) }
	}

fun <F, T> Parser<F>.map(fn: (F) -> T): Parser<T> =
	when (this) {
		is PartialParser<*, F> -> PartialParser(input) { this@map.parse(it)?.map(fn) }
		is DoneParser -> DoneParser(fn(parsed))
	}

fun <T> Parser<T>.parse(bit: Bit): Parser<T>? =
	when (this) {
		is DoneParser -> parseError()
		is PartialParser<*, T> -> this.parse(bit)
	}

fun <T> Parser<T>.parse(bits: Iterable<Bit>): Parser<T>? =
	bits.fold(orNull) { parser, bit -> parser?.parse(bit) }

fun <T> parseError(): Parser<T>? = null

fun <F, T> PartialParser<F, T>.parse(bit: Bit): Parser<T>? =
	parseFn(input, bit)

