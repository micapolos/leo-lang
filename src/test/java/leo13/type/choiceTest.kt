package leo13.type

import leo.base.assertEqualTo
import leo13.script.lineTo
import leo13.script.script
import kotlin.test.Test

class OptionsTest {
	@Test
	fun scriptingLine() {
		options()
			.scriptingLine
			.assertEqualTo("options" lineTo script())

		options(
			"zero" lineTo type("foo"),
			"one" lineTo type("bar"))
			.scriptingLine
			.assertEqualTo(
				"options" lineTo script(
					"zero" lineTo script("foo"),
					"one" lineTo script("bar")))
	}

	@Test
	fun containsLine() {
		options("zero", "one")
			.contains("zero" lineTo type())
			.assertEqualTo(true)
	}

	@Test
	fun containsOptions() {
		options("zero", "one")
			.contains(options("zero", "one"))
			.assertEqualTo(true)
	}

	@Test
	fun containsLineType() {
		options("zero", "one")
			.contains(type("zero" lineTo type()))
			.assertEqualTo(true)
	}

	@Test
	fun containsOptionsType() {
		options("zero", "one")
			.contains(type(options("zero", "one")))
			.assertEqualTo(true)
	}
}