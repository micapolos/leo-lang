package leo32.runtime

import leo.base.assertContains
import leo.base.assertEqualTo
import leo.base.empty
import leo32.base.add
import leo32.base.list
import leo32.base.seq
import kotlin.test.Test

class DictionaryTest {
	@Test
	fun putAt() {
		empty
			.dictionary<Int>()
			.put(term("zero"), 0)
			.put(term("one"), 1)
			.at(term("zero"))
			.assertEqualTo(0)

		empty
			.dictionary<Int>()
			.put(term("zero"), 0)
			.put(term("one"), 1)
			.at(term("one"))
			.assertEqualTo(1)

		empty
			.dictionary<Int>()
			.put(term("zero"), 0)
			.put(term("one"), 1)
			.at(term("two"))
			.assertEqualTo(null)
	}

	@Test
	fun foldKeyAndValuePairs() {
		val dictionary = empty
			.dictionary<Int>()
			.put(term("zero"), 0)
			.put(term("one"), 1)

		list<Pair<Term, Int>>()
			.foldPairs(dictionary) { add(it) }
			.seq
			.assertContains(
				term("one") to 1,
				term("zero") to 0)
	}
}