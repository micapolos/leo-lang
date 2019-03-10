package leo.base

import org.junit.Test

class SeqNodeOrEmptyTest {
	@Test
	fun content() {
		seq<Int>().assertContains()
		seq(1).assertContains(1)
		seq(1, 2, 3).assertContains(1, 2, 3)
	}

	@Test
	fun iterator() {
		seq<Int>().assertContains()
		seq(1).assertContains(1)
		seq(1, 2, 3).assertContains(1, 2, 3)
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