package leo.base

data class Stream<V>(
	val first: V,
	val nextOrNullFn: () -> Stream<V>?) {

	data class Folded<V, R>(
		val foldedFirst: R,
		val nextOrNull: Stream<V>?
	)
}

val <V> V.onlyStream
	get() =
		Stream(this) { null }

val <V> Stream<V>.nextOrNull: Stream<V>?
	get() =
		nextOrNullFn()

fun <V> Stream<V>.plus(value: V) =
	Stream(value) { this }

fun <V> Stream<V>.then(stream: Stream<V>): Stream<V> =
	Stream(first) { nextOrNull?.then(stream) ?: stream }

fun <V> Stream<V>.thenIfNotNull(streamOrNull: Stream<V>?): Stream<V> =
	foldIfNotNull(streamOrNull, Stream<V>::then)

fun <V> Stream<V>?.orNullThen(stream: Stream<V>): Stream<V> =
	this?.then(stream) ?: stream

fun <V> Stream<V>?.orNullThenIfNotNull(streamOrNull: Stream<V>?): Stream<V>? =
	if (streamOrNull == null) this else orNullThen(streamOrNull)

val <V> Stream<Stream<V>>.join: Stream<V>
	get() =
		foldFirst { it }.foldNext { then(it) }

fun <V> stream(first: V, vararg next: V) =
	stack(first, *next).reverse.stream

fun <V, R> Stream<V>.foldFirst(fn: (V) -> R): Stream.Folded<V, R> =
	Stream.Folded(fn(first), nextOrNullFn())

fun <V, R> Stream.Folded<V, R>.foldNext(fn: R.(V) -> R): R =
	foldedFirst.fold(nextOrNull, fn)

tailrec fun <V, R> R.fold(streamOrNull: Stream<V>?, foldNext: R.(V) -> R): R =
	if (streamOrNull == null) this
	else foldNext(streamOrNull.first).fold(streamOrNull.nextOrNullFn(), foldNext)

fun <V, R> Stream<V>.map(fn: (V) -> R): Stream<R> =
	Stream(fn(first)) { nextOrNull?.map(fn) }

fun <V, R> Stream<V>.mapNotNull(fn: (V) -> R?): Stream<R>? =
	fn(first)?.let { mapped ->
		Stream(mapped) {
			nextOrNull?.mapNotNull(fn)
		}
	} ?: nextOrNull?.mapNotNull(fn)

val <V> Stream<V>.stack: Stack<V>
	get() =
		foldFirst { it.onlyStack }.foldNext { push(it) }

val <V> Stream<V>.reverse: Stream<V>
	get() =
		stack.stream

val <V> Stack<V>.stream: Stream<V>
	get() =
		Stream(top) { pop?.stream }
