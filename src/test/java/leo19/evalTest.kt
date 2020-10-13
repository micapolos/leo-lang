package leo19

import leo.base.assertEqualTo
import leo14.script
import kotlin.test.Test

class EvalTest {
	@Test
	fun endToEnd() {
		script("zero").eval.assertEqualTo(script("zero"))
	}
}