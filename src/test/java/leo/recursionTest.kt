package leo

import leo.base.assertEqualTo
import leo.base.goBack
import leo.base.string
import kotlin.test.Test

class RecurseTest {
	@Test
	fun string() {
		recurse(goBack, goBack)
			.string
			.assertEqualTo("recurse back, recurse back")
	}

	@Test
	fun backApply() {
		recurse(goBack)
			.apply(backTrace(oneWord.term(), twoWord.term(), threeWord.term()))
			.assertEqualTo(backTrace(oneWord.term(), twoWord.term()))
	}

	@Test
	fun backBackApply() {
		recurse(goBack, goBack)
			.apply(backTrace(oneWord.term(), twoWord.term(), threeWord.term()))
			.assertEqualTo(backTrace(oneWord.term()))
	}

	@Test
	fun overflowApply() {
		recurse(goBack, goBack, goBack)
			.apply(backTrace(oneWord.term(), twoWord.term(), threeWord.term()))
			.assertEqualTo(null)
	}
}