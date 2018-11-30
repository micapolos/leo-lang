package leo

import leo.base.assertEqualTo
import leo.base.string
import kotlin.test.Test

class BackTraceTest {
	@Test
	fun string() {
		backTrace(oneWord.term, twoWord.term)
			.string
			.assertEqualTo("back trace(the one, the two)")
	}
}