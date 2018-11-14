package leo.base

data class Stream<V>(
	val first: V,
	val nextFn: () -> Stream<V>?) {

	data class Folded<V, R>(
		val foldedFirst: R,
		val next: Stream<V>?
	)
}

val <V> Stream<V>.next
	get() =
		nextFn()

fun <V> Stream<V>.plus(value: V) =
	Stream(value) { this }

fun <V> stream(first: V, vararg next: V) =
	stack(first, *next).stream

fun <V, R> Stream<V>.foldFirst(fn: (V) -> R): Stream.Folded<V, R> =
	Stream.Folded(fn(first), nextFn())

fun <V, R> Stream.Folded<V, R>.foldNext(fn: R.(V) -> R): R =
	foldedFirst.fold(next, fn)

tailrec fun <V, R> R.fold(streamOrNull: Stream<V>?, foldNext: R.(V) -> R): R =
	if (streamOrNull == null) this
	else foldNext(streamOrNull.first).fold(streamOrNull.nextFn(), foldNext)

fun <V, R> Stream<V>.map(fn: (V) -> R): Stream<R> =
	Stream(fn(first)) { next?.map(fn) }

fun <V, R> Stream<V>.mapNotNull(fn: (V) -> R?): Stream<R>? =
	fn(first)?.let { mapped ->
		Stream(mapped) {
			next?.mapNotNull(fn)
		}
	} ?: next?.mapNotNull(fn)

val <V> Stream<V>.reversedStack
	get() =
		foldFirst { it.stack }.foldNext { push(it) }

val <V> Stream<V>.stack
	get() =
		reversedStack.reverse

val <V> Stack<V>.stream: Stream<V>
	get() =
		Stream(top) { pop?.stream }