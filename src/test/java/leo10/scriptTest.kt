package leo10

import leo.base.assertEqualTo
import leo.base.empty
import leo.base.string
import kotlin.test.Test

class ScriptTest {
	@Test
	fun string() {
		script().string.assertEqualTo("")
		script("one" lineTo script()).string.assertEqualTo("one()")
		script(
			"one" lineTo script(),
			"plus" lineTo script(
				"two" lineTo script()))
			.string
			.assertEqualTo("one()plus(two())")
		script(function(empty)).string.assertEqualTo("*")
	}
}