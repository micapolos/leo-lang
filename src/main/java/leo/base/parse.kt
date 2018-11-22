package leo.base

data class Parse<V, P>(
	val streamOrNull: Stream<V>?,
	val parsed: P)

infix fun <V, P> Stream<V>?.parsed(value: P) =
	Parse(this, value)

fun <V, P, R> Parse<V, P>.map(fn: (P) -> R): Parse<V, R> =
	Parse(streamOrNull, fn(parsed))

fun <V, P, R> Parse<V, P>?.bind(fn: Stream<V>?.(P) -> Parse<V, R>?): Parse<V, R>? =
	if (this == null) null
	else streamOrNull.fn(parsed)

val <V> Stream<V>?.parseItself: Parse<V, V>?
	get() =
		if (this == null) null
		else nextOrNull?.parsed(first)

val <V, P> Parse<V, P>.theParsed: The<P>?
	get() =
		streamOrNull.ifNull { parsed.the }