package leo.base

fun <V : Any> iterator(nextFn: () -> V?): Iterator<V> =
	object : Iterator<V> {
		var hasPreparedNext = false
		var preparedNext: V? = null

		override fun hasNext(): Boolean {
			prepareNext()
			return preparedNext != null
		}

		override fun next(): V {
			prepareNext()
			val next = preparedNext!!
			hasPreparedNext = false
			preparedNext = null
			return next
		}

		private fun prepareNext() {
			if (!hasPreparedNext) {
				preparedNext = nextFn()
				hasPreparedNext = true
			}
		}
	}

fun <V> emptyIterator(): Iterator<V> =
	object : Iterator<V> {
		override fun hasNext() = false
		override fun next(): V = throw IllegalStateException()
	}

val <V> V.onlyIterator: Iterator<V>
	get() =
		iterator(this)

fun <V> iterator(vararg items: V): Iterator<V> =
	items.iterator()

fun <V> flattenIterator(vararg iterators: Iterator<V>): Iterator<V> =
	flatten(listOf(*iterators).iterator())

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

val <V> Iterator<V>.list: List<V>
	get() {
		val list = ArrayList<V>()
		while (hasNext()) list.add(next())
		return list
	}
