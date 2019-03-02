package leo.base

fun <V> emptyIterator(): Iterator<V> =
	object : Iterator<V> {
		override fun hasNext() = false
		override fun next(): V = throw IllegalStateException()
	}

fun <V> iterator(vararg items: V): Iterator<V> =
	items.iterator()

fun <V> flatten(iteratorsIterator: Iterator<Iterator<V>>): Iterator<V> =
	object : Iterator<V> {
		var currentIteratorOrNull: Iterator<V>? = null

		override fun hasNext() = advance().hasNext()
		override fun next() = advance().next()

		private fun advance(): Iterator<V> {
			while (currentIteratorOrNull == null || !currentIteratorOrNull!!.hasNext()) {
				if (iteratorsIterator.hasNext()) {
					currentIteratorOrNull = iteratorsIterator.next()
				} else {
					currentIteratorOrNull = emptyIterator()
					break
				}
			}
			return currentIteratorOrNull!!
		}
	}
