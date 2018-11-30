package leo

import leo.base.assertEqualTo
import leo.base.string
import kotlin.test.Test

class RecursionTest {
	@Test
	fun string() {
		recursion(goBack, goBack)
			.string
			.assertEqualTo("recursion(go back, go back)")
	}

	@Test
	fun backApply() {
		recursion(goBack)
			.apply(backTrace(oneWord.term(), twoWord.term(), threeWord.term()))
			.assertEqualTo(backTrace(oneWord.term(), twoWord.term()))
	}

	@Test
	fun backBackApply() {
		recursion(goBack, goBack)
			.apply(backTrace(oneWord.term(), twoWord.term(), threeWord.term()))
			.assertEqualTo(backTrace(oneWord.term()))
	}

	@Test
	fun overflowApply() {
		recursion(goBack, goBack, goBack)
			.apply(backTrace(oneWord.term(), twoWord.term(), threeWord.term()))
			.assertEqualTo(null)
	}
}