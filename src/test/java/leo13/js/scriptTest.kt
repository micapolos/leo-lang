package leo13.js

import leo.base.assertEqualTo
import leo.base.fail
import kotlin.test.Test

class ScriptTest {
	@Test
	fun code() {
		script<Plain>(
			item(2),
			item("plus" lineTo script(item(3))))
			.code { fail }
			.assertEqualTo("2.plus(3)")

		script<Plain>(
			item(2.5),
			item("plus" lineTo script(item(3.5))))
			.code { fail }
			.assertEqualTo("2.5.plus(3.5)")
	}
}