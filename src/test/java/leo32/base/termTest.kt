package leo32.base

import leo.base.assertEqualTo
import leo.base.empty
import kotlin.test.Test

class TermTest {
	@Test
	fun test() {
		empty.term.plus("zero").plus("bit")
			.plus("and", empty.term.plus("zero").plus("bit"))
			.plus("gives", empty.term.plus("zero").plus("bit"))
			.assertEqualTo(null)
	}
}