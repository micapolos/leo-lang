package leo13.js.compiler

import leo.base.assertEqualTo
import kotlin.test.Test

class ScriptTest {
	@Test
	fun code() {
		script(
			line(number(2)),
			line("plus" fieldTo 3))
			.code
			.assertEqualTo("2.plus(3)")

		script(
			line(number(2.5)),
			line("plus" fieldTo 3.5))
			.code
			.assertEqualTo("2.5.plus(3.5)")

		script(
			"vec" fieldTo script(
				"x" fieldTo 1,
				"y" fieldTo 2,
				"name" fieldTo "my vector"))
			.code
			.assertEqualTo("vec(x(1).y(2).name(\"my vector\"))")
	}
}