package leo.lab

import leo.base.assertEqualTo
import leo.base.back
import leo.oneWord
import leo.threeWord
import leo.twoWord
import org.junit.Test

class RecursionTest {
	@Test
	fun apply() {
		recursion(back)
			.apply(backTrace(oneWord.script(), twoWord.script(), threeWord.script()))
			.assertEqualTo(backTrace(oneWord.script(), twoWord.script()))

		recursion(back, back)
			.apply(backTrace(oneWord.script(), twoWord.script(), threeWord.script()))
			.assertEqualTo(backTrace(oneWord.script()))

		recursion(back, back, back)
			.apply(backTrace(oneWord.script(), twoWord.script(), threeWord.script()))
			.assertEqualTo(null)
	}
}