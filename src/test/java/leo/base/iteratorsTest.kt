package leo.base

import kotlin.test.Test

class IteratorsTest {
	@Test
	fun nextFnIterator() {
		iterator { null }.assertContains()
		val iterator = listOf(1, 2, 3).iterator()
		iterator {
			if (iterator.hasNext()) iterator.next() else null
		}.assertContains(1, 2, 3)
	}

	@Test
	fun iteratorConstructor() {
		iterator<Int>().assertContains()
		iterator(1).assertContains(1)
		iterator(1, 2, 3).assertContains(1, 2, 3)
	}

	@Test
	fun flatten() {
		flatten<Int>(iterator()).assertContains()
		flatten<Int>(iterator(emptyIterator())).assertContains()
		flatten<Int>(iterator(emptyIterator(), emptyIterator())).assertContains()

		flatten(iterator(iterator(1, 2))).assertContains(1, 2)
		flatten(iterator(iterator(1), iterator(2))).assertContains(1, 2)
		flatten(iterator(emptyIterator(), iterator(1), iterator(2))).assertContains(1, 2)
		flatten(iterator(iterator(1), emptyIterator(), iterator(2))).assertContains(1, 2)
		flatten(iterator(iterator(1), iterator(2), emptyIterator())).assertContains(1, 2)
		flatten(iterator(iterator(1, 2), iterator(3, 4))).assertContains(1, 2, 3, 4)
	}
}

fun <T> Iterator<T>.assertContains(vararg items: T) {
	for (item in items) {
		hasNext().assertEqualTo(true)
		hasNext().assertEqualTo(true)
		next().assertEqualTo(item)
	}
	hasNext().assertEqualTo(false)
	hasNext().assertEqualTo(false)
}