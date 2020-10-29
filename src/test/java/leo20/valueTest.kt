package leo20

import leo.base.assertEqualTo
import kotlin.test.Test

class ValueTest {
	@Test
	fun unsafeNumberPlus() {
		value(line(2))
			.unsafeNumberPlus(value(line(3)))
			.assertEqualTo(value(line(5)))
	}

	@Test
	fun lineOrNull() {
		val value = value(
			"point" lineTo value(
				"x" lineTo value(line(10)),
				"y" lineTo value(line(20))))

		value.lineOrNull("point").assertEqualTo("point" lineTo value(
			"x" lineTo value(line(10)),
			"y" lineTo value(line(20))))
		value.lineOrNull("point", "x").assertEqualTo("x" lineTo value(line(10)))
		value.lineOrNull("point", "y").assertEqualTo("y" lineTo value(line(20)))
		value.lineOrNull("point", "x", "number").assertEqualTo(line(10))
		value.lineOrNull("point", "y", "number").assertEqualTo(line(20))
	}

	@Test
	fun getOrNull() {
		val value = value(
			"point" lineTo value(
				"x" lineTo value(line(10)),
				"y" lineTo value(line(20))))

		value.getOrNull("x").assertEqualTo(value("x" lineTo value(line(10))))
		value.getOrNull("y").assertEqualTo(value("y" lineTo value(line(20))))
		value.getOrNull("x", "number").assertEqualTo(value(line(10)))
		value.getOrNull("y", "number").assertEqualTo(value(line(20)))
	}
}