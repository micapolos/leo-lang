package leo.base

import kotlin.test.Test

class SeqTest {
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

	@Test
	fun map() {
		seq(1, 2, 3).map { toString() }.assertContains("1", "2", "3")
	}

	@Test
	fun flatten() {
		seq<Seq<Int>>().flat.assertContains()
		seq(seq<Int>()).flat.assertContains()
		seq(seq<Int>(), seq()).flat.assertContains()
		seq(seq(1), seq(2)).flat.assertContains(1, 2)
		seq(seq(1, 2), seq(3, 4)).flat.assertContains(1, 2, 3, 4)
		seq(seq(), seq(1, 2), seq(), seq(3, 4), seq()).flat.assertContains(1, 2, 3, 4)
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