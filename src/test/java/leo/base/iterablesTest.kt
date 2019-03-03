package leo.base

import org.junit.Test

class IterablesTest {
	@Test
	fun allOf() {
		all<Int>().assertContains()
		all(1, 2, 3).assertContains(1, 2, 3)
	}
}

fun <T> Iterable<T>.assertContains(vararg items: T) {
	iterator().assertContains(*items)
}