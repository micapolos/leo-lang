package leo10

import leo.base.assertEqualTo
import leo.base.string
import kotlin.test.Test

class InterpreterTest {
	@Test
	fun test() {
		script("foo" lineTo script())
			.eval
			.string
			.assertEqualTo("foo()")

		script(
			"circle" lineTo script(
				"radius" lineTo script(
					"zero" lineTo script()),
				"center" lineTo script()),
			"radius" lineTo script())
			.eval
			.assertEqualTo(script("radius" lineTo script("zero" lineTo script())))
	}
}