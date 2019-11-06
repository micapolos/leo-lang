package leo14

import leo.base.assertEqualTo
import kotlin.test.Test

class ScriptTest {
	@Test
	fun code() {
		script(
			line(number(2)),
			line("plus" fieldTo literal(3)))
			.code
			.assertEqualTo("2.plus(3)")

		script(
			line(number(2.5)),
			line("plus" fieldTo literal(3.5)))
			.code
			.assertEqualTo("2.5.plus(3.5)")

		script(
			"vec" fieldTo script(
				"x" fieldTo literal(1),
				"y" fieldTo literal(2),
				"name" fieldTo literal("my vector")))
			.code
			.assertEqualTo("vec(x(1).y(2).name(\"my vector\"))")
	}
}