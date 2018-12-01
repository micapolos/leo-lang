package leo.base

sealed class Parser<in I, out O>

data class PushParser<I, O>(
	val pushFn: (I) -> Parser<I, O>?) : Parser<I, O>()

data class DoneParser<I, O>(
	val parsed: O) : Parser<I, O>()

fun <I, O> pushParser(fn: (I) -> Parser<I, O>?): PushParser<I, O> =
	PushParser(fn)

fun <I, O> O.doneParser(): Parser<I, O> =
	DoneParser(this)

fun <I, O> Parser<I, O>.push(value: I): Parser<I, O>? =
	pushParserOrNull?.push(value)

fun <I, O> PushParser<I, O>.push(value: I): Parser<I, O>? =
	pushFn(value)

// === casting

val <I, O> Parser<I, O>.pushParserOrNull: PushParser<I, O>?
	get() =
		this as? PushParser<I, O>

val <I, O> Parser<I, O>.doneParserOrNull: DoneParser<I, O>?
	get() =
		this as? DoneParser<I, O>

val <I, O : Any> Parser<I, O>.parsedOrNull: O?
	get() =
		doneParserOrNull?.parsed