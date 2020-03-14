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
	fun numberAddNumber() {
		script(
			line(literal(10)),
			"add" lineTo script(literal(20)))
			.eval
			.assertEqualTo(script(literal(30)))
	}

	@Test
	fun numberSubtractNumber() {
		script(
			line(literal(30)),
			"subtract" lineTo script(literal(20)))
			.eval
			.assertEqualTo(script(literal(10)))
	}

	@Test
	fun numberMultiplyByNumber() {
		script(
			line(literal(2)),
			"multiply" lineTo script(),
			"by" lineTo script(literal(3)))
			.eval
			.assertEqualTo(script(literal(6)))
	}

	@Test
	fun textAppendText() {
		script(
			line(literal("Hello, ")),
			"append" lineTo script(literal("world!")))
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
}