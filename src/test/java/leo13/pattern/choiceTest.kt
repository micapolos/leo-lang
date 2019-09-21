package leo13.pattern

import leo.base.assertEqualTo
import leo13.script.lineTo
import leo13.script.script
import kotlin.test.Test

class ChoiceTest {
	@Test
	fun scriptingLine() {
		choice()
			.scriptingLine
			.assertEqualTo("choice" lineTo script())

		choice(
			"zero" lineTo pattern("foo"),
			"one" lineTo pattern("bar"))
			.scriptingLine
			.assertEqualTo(
				"choice" lineTo script(
					"either" lineTo script("zero" lineTo script("foo")),
					"either" lineTo script("one" lineTo script("bar"))))
	}

	@Test
	fun containsLine() {
		choice("zero", "one")
			.contains("zero" lineTo pattern())
			.assertEqualTo(true)
	}

	@Test
	fun containsChoice() {
		choice("zero", "one")
			.contains(choice("zero", "one"))
			.assertEqualTo(true)
	}

	@Test
	fun containsLinePattern() {
		choice("zero", "one")
			.contains(pattern("zero" lineTo pattern()))
			.assertEqualTo(true)
	}

	@Test
	fun containsChoicePattern() {
		choice("zero", "one")
			.contains(pattern(choice("zero", "one")))
			.assertEqualTo(true)
	}
}