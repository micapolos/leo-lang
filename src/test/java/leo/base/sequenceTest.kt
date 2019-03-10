package leo.base

import org.junit.Test

class SeqNodeOrEmptyTest {
	@Test
	fun content() {
		sequence<Int>().assertContains()
		sequence(1).assertContains(1)
		sequence(1, 2, 3).assertContains(1, 2, 3)
	}

	@Test
	fun iterator() {
		sequence<Int>().assertContains()
		sequence(1).assertContains(1)
		sequence(1, 2, 3).assertContains(1, 2, 3)
	}
}

fun <T> Seq<T>.assertContains(vararg items: T) {
	val list = ArrayList<T>()
	var sequence = this
	while (true) {
		val nonEmptySequence = sequence.seqNodeOrNullFn() ?: break
		list.add(nonEmptySequence.first)
		sequence = nonEmptySequence.remaining
	}
	list.assertEqualTo(listOf(*items))
}