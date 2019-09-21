package leo13.pattern

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
			"zero" lineTo pattern("foo"),
			"one" lineTo pattern("bar"))
			.scriptingLine
			.assertEqualTo(
				"options" lineTo script(
					"zero" lineTo script("foo"),
					"one" lineTo script("bar")))
	}

	@Test
	fun containsLine() {
		options("zero", "one")
			.contains("zero" lineTo pattern())
			.assertEqualTo(true)
	}

	@Test
	fun containsOptions() {
		options("zero", "one")
			.contains(options("zero", "one"))
			.assertEqualTo(true)
	}

	@Test
	fun containsLinePattern() {
		options("zero", "one")
			.contains(pattern("zero" lineTo pattern()))
			.assertEqualTo(true)
	}

	@Test
	fun containsOptionsPattern() {
		options("zero", "one")
			.contains(pattern(options("zero", "one")))
			.assertEqualTo(true)
	}
}