package leo13.untyped.value

import leo.base.assertEqualTo
import kotlin.test.Test

class ValueTest {
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