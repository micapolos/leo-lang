package leo13.type

import leo.base.assertEqualTo
import kotlin.test.Test

class TypeTest {
	@Test
	fun lhsOrNull() {
		type()
			.previousOrNull
			.assertEqualTo(null)

		type("one" lineTo type())
			.previousOrNull
			.assertEqualTo(type())

		type("one" lineTo type(), "two" lineTo type())
			.previousOrNull
			.assertEqualTo(type("one" lineTo type()))

		type(unsafeChoice("one" caseTo type(), "two" caseTo type()))
			.previousOrNull
			.assertEqualTo(type())

		type(arrow(type("one"), type("two")))
			.previousOrNull
			.assertEqualTo(type())
	}

	@Test
	fun setOrNull() {
		val type = type(
			"vec" lineTo type(
				"x" lineTo type("zero"),
				"y" lineTo type("one")))

		type
			.setOrNull("x" lineTo type("ten"))
			.assertEqualTo(
				type(
					"vec" lineTo type(
						"x" lineTo type("ten"),
						"y" lineTo type("one"))))

		type
			.setOrNull("y" lineTo type("ten"))
			.assertEqualTo(
				type(
					"vec" lineTo type(
						"x" lineTo type("zero"),
						"y" lineTo type("ten"))))

		type
			.setOrNull("z" lineTo type("ten"))
			.assertEqualTo(null)
	}
}