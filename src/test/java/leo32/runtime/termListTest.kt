package leo32.runtime

import leo.base.assertEqualTo
import kotlin.test.Test

class TermListTest {
	@Test
	fun termList() {
		termList("bit", term("zero"), term("one"), term("two"))
			.apply { size.assertEqualTo(3) }
			.apply { at(0).assertEqualTo(term("zero")) }
			.apply { at(1).assertEqualTo(term("one")) }
			.apply { at(2).assertEqualTo(term("two")) }
	}

	@Test
	fun termListOrNull() {
		termListOrNull("bit" to term("zero"), "bit" to term("one"))
			.assertEqualTo(termList("bit", term("zero"), term("one")))

		termListOrNull("bit" to term("zero"), "byte" to term("one"))
			.assertEqualTo(null)
	}

	@Test
	fun plusTerm() {
		termList("bit", term("zero"), term("one"))
			.plus(term("two"))
			.assertEqualTo(termList("bit", term("zero"), term("one"), term("two")))
	}

	@Test
	fun plusField() {
		termList("bit", term("zero"), term("one"))
			.plus("bit" to term("two"))
			.assertEqualTo(termList("bit", term("zero"), term("one"), term("two")))

		termList("bit", term("zero"), term("one"))
			.plus("byte" to term("two"))
			.assertEqualTo(null)
	}
}