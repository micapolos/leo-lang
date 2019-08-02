package leo12

import leo.base.assertEqualTo
import leo.base.empty
import leo9.stack
import kotlin.test.Test

class ScriptTest {
	@Test
	fun constructors() {
		scriptBody("zero")
		body("zero", "plus" lineTo scriptBody("one"), "plus" lineTo scriptBody("two"))
		body("color" lineTo scriptBody("red"))
		body("x" lineTo scriptBody("zero"), "y" lineTo scriptBody("one"))

		body("circle" lineTo body(
			"radius" lineTo body(
				"x" lineTo scriptBody("zero"),
				"y" lineTo scriptBody("one")),
			"center" lineTo body(
				"zero",
				"plus" lineTo scriptBody("one"))))
	}

	@Test
	fun bodyStackOrNull() {
		script(empty)
			.bodyStackOrNull("either")
			.assertEqualTo(stack())

		scriptBody("either")
			.bodyStackOrNull("either")
			.assertEqualTo(null)

		body("either" lineTo scriptBody("one"))
			.bodyStackOrNull("either")
			.assertEqualTo(stack(scriptBody("one")))

		body(
			"either" lineTo scriptBody("one"),
			"either" lineTo scriptBody("two"))
			.bodyStackOrNull("either")
			.assertEqualTo(stack(scriptBody("one"), scriptBody("two")))
	}
}