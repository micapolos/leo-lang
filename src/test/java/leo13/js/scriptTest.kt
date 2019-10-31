package leo13.js

import leo.base.assertEqualTo
import leo.base.fail
import kotlin.test.Test

class ScriptTest {
	@Test
	fun code() {
		script<Plain>(
			line(2),
			line("plus" fieldTo 3))
			.code { fail }
			.assertEqualTo("2.plus(3)")

		script<Plain>(
			line(2.5),
			line("plus" fieldTo 3.5))
			.code { fail }
			.assertEqualTo("2.5.plus(3.5)")

		script<Plain>(
			"vec" fieldTo script(
				"x" fieldTo 1,
				"y" fieldTo 2))
			.code { fail }
			.assertEqualTo("vec(x(1).y(2))")
	}
}