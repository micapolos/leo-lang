package leo

import leo.base.assertEqualTo
import leo.base.back
import leo.base.string
import kotlin.test.Test

class RecurseTest {
	@Test
	fun string() {
		recurse(back, back)
			.string
			.assertEqualTo("recurse back, recurse back")
	}

	@Test
	fun backApply() {
		recurse(back)
			.apply(backTrace(oneWord.term(), twoWord.term(), threeWord.term()))
			.assertEqualTo(backTrace(oneWord.term(), twoWord.term()))
	}

	@Test
	fun backBackApply() {
		recurse(back, back)
			.apply(backTrace(oneWord.term(), twoWord.term(), threeWord.term()))
			.assertEqualTo(backTrace(oneWord.term()))
	}

	@Test
	fun overflowApply() {
		recurse(back, back, back)
			.apply(backTrace(oneWord.term(), twoWord.term(), threeWord.term()))
			.assertEqualTo(null)
	}
}