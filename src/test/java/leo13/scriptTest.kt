package leo13

import leo.base.assertEqualTo
import kotlin.test.Test

class ScriptTest {
	@Test
	fun code() {
		script().code.assertEqualTo("")
		script("one" lineTo script()).code.assertEqualTo("one()")
		script("one" lineTo script(), "two" lineTo script()).code.assertEqualTo("one()two()")
		script("one" lineTo script("two" lineTo script())).code.assertEqualTo("one(two())")
	}
}