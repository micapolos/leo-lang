package leo14

sealed class Parser<in I, out O, out E>

data class WriteParser<I, O, E>(val write: (I) -> Parser<I, O, E>) : Parser<I, O, E>()
data class ResultParser<I, O, E>(val result: O) : Parser<I, O, E>()
data class ErrorParser<I, O, E>(val error: E) : Parser<I, O, E>()

fun <I, O, E> parser(write: (I) -> Parser<I, O, E>): Parser<I, O, E> = WriteParser(write)
fun <I, O, E> resultParser(result: O): Parser<I, O, E> = ResultParser(result)
fun <I, O, E> errorParser(error: E): Parser<I, O, E> = ErrorParser(error)

val <I, O, E> Parser<I, O, E>.unsafeResult: O
	get() =
		when (this) {
			is WriteParser -> error("parse incomplete")
			is ResultParser -> result
			is ErrorParser -> error("parse error: $error")
		}
