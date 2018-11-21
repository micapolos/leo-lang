package leo.base

data class Parse<V, P>(
	val streamOrNull: Stream<V>?,
	val parsed: P)

infix fun <V, P> Stream<V>?.parsed(value: P) =
	Parse(this, value)

fun <V, P, R> Parse<V, P>.map(fn: (P) -> R): Parse<V, R> =
	Parse(streamOrNull, fn(parsed))

fun <V, P, R> Parse<V, P>.bind(fn: Stream<V>?.(P) -> Parse<V, R>?): Parse<V, R>? =
	streamOrNull.fn(parsed)?.let { parse ->
		Parse(parse.streamOrNull, parse.parsed)
	}