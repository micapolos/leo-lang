package leo11

import leo.base.assertEqualTo
import kotlin.test.Test

class ScriptTest {
	@Test
	fun code() {
		script().code.assertEqualTo("")
		script("one" lineTo script()).code.assertEqualTo("one()")
		script("one" lineTo script(
			"two" lineTo script())).code.assertEqualTo("one(two())")
		script(
			"one" lineTo script(),
			"two" lineTo script()).code.assertEqualTo("two(one())")
		script(
			"one" lineTo script(),
			"plus" lineTo script(
				"two" lineTo script())).code.assertEqualTo("one()plus(two())")
		script(
			"x" lineTo script(
				"one" lineTo script()),
			"y" lineTo script(
				"two" lineTo script())).code.assertEqualTo("x(one())y(two())")
		script(
			"x" lineTo script(
				"one" lineTo script()),
			"y" lineTo script(
				"two" lineTo script()),
			"vec" lineTo script()).code.assertEqualTo("vec(x(one())y(two()))")
	}
}