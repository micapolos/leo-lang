package leo.base

fun <V> all(vararg items: V): Iterable<V> =
	listOf(*items)

fun <V> flattenIterators(iterable: Iterable<Iterator<V>>) =
	flatten(iterable.iterator())

fun <T> iterable(iteratorFn: () -> Iterator<T>): Iterable<T> =
	object : Iterable<T> {
		override fun iterator() = iteratorFn()
	}