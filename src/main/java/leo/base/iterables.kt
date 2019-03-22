package leo.base

fun <V> all(vararg items: V): Iterable<V> =
	listOf(*items)

fun <V> flattenIterators(iterable: Iterable<Iterator<V>>) =
	flatten(iterable.iterator())

fun <T> iterable(iteratorFn: () -> Iterator<T>): Iterable<T> =
	object : Iterable<T> {
		override fun iterator() = iteratorFn()
	}

fun <T, R> R.fold(iterable: Iterable<T>, fn: R.(T) -> R): R =
	iterable.fold(this) { folded, value -> folded.fn(value) }