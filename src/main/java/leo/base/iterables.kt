package leo.base

fun <V> all(vararg items: V): Iterable<V> =
	listOf(*items)

fun <V> flattenIterators(iterable: Iterable<Iterator<V>>) =
	flatten(iterable.iterator())
