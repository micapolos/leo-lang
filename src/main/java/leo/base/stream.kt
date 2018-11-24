package leo.base

data class Stream<out V>(
	val first: V,
	val nextOrNullFn: () -> Stream<V>?)

val <V> V.onlyStream: Stream<V>
	get() =
		Stream(this) { null }

val <V> Stream<V>.nextOrNull: Stream<V>?
	get() = nextOrNullFn()

fun <V> V.then(nextOrNullFn: () -> Stream<V>?): Stream<V> =
	Stream(this) { nextOrNullFn() }

fun <V> Stream<V>.then(nextOrNullFn: () -> Stream<V>?): Stream<V> =
	first.then { nextOrNull?.then(nextOrNullFn) ?: nextOrNullFn() }

fun <V> Stream<V>?.orNullThen(nextOrNullFn: () -> Stream<V>?): Stream<V>? =
	this?.then(nextOrNullFn) ?: nextOrNullFn()

val <V> Stream<Stream<V>>.join: Stream<V>
	get() = first.then { nextOrNull?.join }

val <V> Stream<Stream<V>?>.joinOrNull: Stream<V>?
	get() = first?.then { nextOrNull?.joinOrNull } ?: nextOrNull?.joinOrNull

fun <V> stream(first: V, vararg next: V) =
	stack(first, *next).reverse.stream

tailrec fun <V, R> R.fold(stream: Stream<V>?, fn: R.(V) -> R): R =
	if (stream == null) this
	else fn(stream.first).fold(stream.nextOrNull, fn)

fun <V, R> Stream<V>.map(fn: (V) -> R): Stream<R> =
	fn(first).then { nextOrNull?.map(fn) }

fun <V, R> Stream<V>.mapJoin(fn: (V) -> Stream<R>): Stream<R> =
	map(fn).join

fun <V, R : Any> Stream<V>.filterMap(fn: (V) -> R?): Stream<R>? {
	val mappedFirst = fn(first)
	val nextOrNull = nextOrNull
	return mappedFirst?.then { nextOrNull?.filterMap(fn) } ?: nextOrNull?.filterMap(fn)
}

fun <V : Any> Stream<V>.all(fn: (V) -> Boolean): Stream<V>? =
	filterMap { value -> if (fn(value)) value else null }

tailrec fun <V : Any> Stream<V>.first(fn: (V) -> Boolean): V? =
	if (fn(first)) first
	else nextOrNull?.first(fn)

val <V> Stream<V>.stack: Stack<V>
	get() =
		first.onlyStack.fold(nextOrNull, Stack<V>::push)

val <V : Any> Stream<V>.onlyValueOrNull: V?
	get() =
		if (nextOrNull == null) first
		else null

fun <V> Stream<V>.intersperse(separator: V): Stream<V> =
	first.then {
		nextOrNull?.map { separator.then { it.onlyStream } }?.join
	}

fun <V, R : Any> Stream<V>.matchFirst(predicate: (V) -> Boolean, fn: Stream<V>?.(V) -> R?): R? =
	nextOrNull.let { nextOrNull ->
		if (predicate(first)) nextOrNull.fn(first)
		else null
	}

fun <V, R : Any> Stream<V>.matchFirst(fn: Stream<V>?.(V) -> R?): R? =
	nextOrNull.fn(first)

fun <V, R : Any> Stream<V>.matchOne(fn: (V) -> R?): R? =
	matchFirst { first ->
		matchNull {
			fn(first)
		}
	}

fun <V, R : Any> Stream<V>.matchTwo(fn: (V, V) -> R?): R? =
	matchFirst { first ->
		this?.matchFirst { second ->
			matchNull {
				fn(first, second)
			}
		}
	}
