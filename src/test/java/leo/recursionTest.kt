package leo

import leo.base.assertEqualTo
import leo.base.string
import kotlin.test.Test

class RecursionTest {
	@Test
	fun string() {
		recursion(back, back, back)
			.string
			.assertEqualTo("recurse back, recurse back, recurse back")
	}

	@Test
	fun backApply() {
		recursion(back)
			.apply(backTrace(oneWord.term(), twoWord.term(), threeWord.term()))
			.assertEqualTo(backTrace(oneWord.term(), twoWord.term()))
	}

	@Test
	fun backBackApply() {
		recursion(back, back)
			.apply(backTrace(oneWord.term(), twoWord.term(), threeWord.term()))
			.assertEqualTo(backTrace(oneWord.term()))
	}

	@Test
	fun overflowApply() {
		recursion(back, back, back)
			.apply(backTrace(oneWord.term(), twoWord.term(), threeWord.term()))
			.assertEqualTo(null)
	}
}