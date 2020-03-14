package leo14.untyped

import leo.base.assertEqualTo
import leo14.*
import kotlin.test.Test

class EvalTest {
	@Test
	fun raw() {
		script(
			"zero" lineTo script(),
			"plus" lineTo script(
				"one" lineTo script()))
			.eval
			.assertEqualTo(
				script(
					"zero" lineTo script(),
					"plus" lineTo script(
						"one" lineTo script())))
	}

	@Test
	fun numberPlusNumber() {
		script(
			line(literal(10)),
			"plus" lineTo script(literal(20)))
			.eval
			.assertEqualTo(script(literal(30)))
	}

	@Test
	fun numberMinusNumber() {
		script(
			line(literal(30)),
			"minus" lineTo script(literal(20)))
			.eval
			.assertEqualTo(script(literal(10)))
	}

	@Test
	fun numberTimesNumber() {
		script(
			line(literal(2)),
			"times" lineTo script(literal(3)))
			.eval
			.assertEqualTo(script(literal(6)))
	}

	@Test
	fun deepMath() {
		script(
			line(literal(2)),
			"plus" lineTo script(
				line(literal(3)),
				"times" lineTo script(literal(4))))
			.eval
			.assertEqualTo(script(literal(14)))
	}

	@Test
	fun textPlusText() {
		script(
			line(literal("Hello, ")),
			"plus" lineTo script(literal("world!")))
			.eval
			.assertEqualTo(script(literal("Hello, world!")))
	}

	@Test
	fun access() {
		val point = script(
			"point" lineTo script(
				"x" lineTo script(literal(10)),
				"y" lineTo script(literal(20))))

		point
			.plus("x" lineTo script())
			.eval
			.assertEqualTo(
				script("x" lineTo script(literal(10))))

		point
			.plus("y" lineTo script())
			.eval
			.assertEqualTo(
				script("y" lineTo script(literal(20))))

		point
			.plus("z" lineTo script())
			.eval
			.assertEqualTo(point.plus("z" lineTo script()))
	}

	@Test
	fun accessNumber() {
		script(
			"x" lineTo script(literal(10)),
			"number" lineTo script())
			.eval
			.assertEqualTo(script(literal(10)))
	}

	@Test
	fun accessText() {
		script(
			"x" lineTo script(literal("foo")),
			"text" lineTo script())
			.eval
			.assertEqualTo(script(literal("foo")))
	}

	@Test
	fun gives() {
		script(
			"number" lineTo script(),
			"gives" lineTo script("number"))
			.eval
			.assertEqualTo(script())
	}

	@Test
	fun givesAndAccess() {
		script(
			"number" lineTo script(),
			"gives" lineTo script("number"),
			line(literal(10)))
			.eval
			.assertEqualTo(script("number"))
	}

	@Test
	fun does() {
		script(
			"number" lineTo script(),
			"does" lineTo script("given" lineTo script()))
			.eval
			.assertEqualTo(script())
	}

	@Test
	fun doesAndAccess() {
		script(
			"number" lineTo script(),
			"does" lineTo script("given" lineTo script()),
			line(literal(10)))
			.eval
			.assertEqualTo(script("given" lineTo script(literal(10))))
	}

	@Test
	fun pattern() {
		val rule = script(
			"false" lineTo script(),
			"or" lineTo script("true"),
			"type" lineTo script(),
			"gives" lineTo script("boolean"))

		rule
			.plus(
				"false" lineTo script(),
				"type" lineTo script())
			.eval
			.assertEqualTo(script("boolean"))

		rule
			.plus(
				"true" lineTo script(),
				"type" lineTo script())
			.eval
			.assertEqualTo(script("boolean"))

		rule
			.plus(
				"maybe" lineTo script(),
				"type" lineTo script())
			.eval
			.assertEqualTo(
				script(
					"maybe" lineTo script(),
					"type" lineTo script()))
	}

	@Test
	fun anythingAppendAnything() {
		script(
			"minus" lineTo script(literal(10)),
			"append" lineTo script("minus" lineTo script(literal(20))))
			.eval
			.assertEqualTo(
				script(
					line(literal(-10)),
					line(literal(-20))))
	}
}