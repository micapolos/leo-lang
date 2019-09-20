package leo13.value

import leo.base.assertEqualTo
import leo13.script.lineTo
import leo13.script.script
import kotlin.test.Test

class ValueTest {
	@Test
	fun scriptLine() {
		value(
			item("x" lineTo value("zero")),
			item(function()),
			item("function" lineTo value("one")))
			.scriptLine
			.assertEqualTo(
				"value" lineTo script(
					item("x" lineTo value("zero")).bodyScriptLine,
					item(function()).bodyScriptLine,
					item("function" lineTo value("one")).bodyScriptLine))
	}

	@Test
	fun replaceLineOrNull() {
		value(
			"x" lineTo value("zero"),
			"y" lineTo value("one"))
			.replaceLineOrNull("x" lineTo value("two"))
			.assertEqualTo(
				value(
					"x" lineTo value("two"),
					"y" lineTo value("one")))

		value(
			"x" lineTo value("zero"),
			"y" lineTo value("one"))
			.replaceLineOrNull("y" lineTo value("two"))
			.assertEqualTo(
				value(
					"x" lineTo value("zero"),
					"y" lineTo value("two")))
	}

	@Test
	fun setOrNull() {
		value("point" lineTo value(
			"x" lineTo value("zero"),
			"y" lineTo value("one")))
			.setOrNull("x" lineTo value("two"))
			.assertEqualTo(
				value("point" lineTo value(
					"x" lineTo value("two"),
					"y" lineTo value("one"))))

		value("point" lineTo value(
			"x" lineTo value("zero"),
			"y" lineTo value("one")))
			.setOrNull("y" lineTo value("two"))
			.assertEqualTo(
				value("point" lineTo value(
					"x" lineTo value("zero"),
					"y" lineTo value("two"))))
	}
}