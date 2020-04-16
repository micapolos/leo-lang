package leo15.type

import leo.base.assertEqualTo
import kotlin.test.Test

class DecompileTest {
	@Test
	fun plus() {
		typed(10.typedLine, "foo".typedLine)
			.assertEqualTo(
				plusTerm(10.term, false, "foo".term, false)
					of type(numberTypeLine, textTypeLine))
	}
}