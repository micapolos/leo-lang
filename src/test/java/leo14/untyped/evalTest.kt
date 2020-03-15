package leo14.untyped

import leo14.*
import kotlin.test.Test

class EvalTest {
	@Test
	fun raw() {
		script(
			"zero" lineTo script(),
			"plus" lineTo script(
				"one" lineTo script()))
			.assertEvalsToThis
	}

	@Test
	fun numberPlusNumber() {
		script(
			line(literal(10)),
			"plus" lineTo script(literal(20)))
			.assertEvalsTo(line(literal(30)))
	}

	@Test
	fun numberMinusNumber() {
		script(
			line(literal(30)),
			"minus" lineTo script(literal(20)))
			.assertEvalsTo(line(literal(10)))
	}

	@Test
	fun numberTimesNumber() {
		script(
			line(literal(2)),
			"times" lineTo script(literal(3)))
			.assertEvalsTo(line(literal(6)))
	}

	@Test
	fun deepMath() {
		script(
			line(literal(2)),
			"plus" lineTo script(
				line(literal(3)),
				"times" lineTo script(literal(4))))
			.assertEvalsTo(line(literal(14)))
	}

	@Test
	fun textPlusText() {
		script(
			line(literal("Hello, ")),
			"plus" lineTo script(literal("world!")))
			.assertEvalsTo(line(literal("Hello, world!")))
	}

	@Test
	fun access() {
		val point = script(
			"point" lineTo script(
				"x" lineTo script(literal(10)),
				"y" lineTo script(literal(20))))

		point
			.plus("x" lineTo script())
			.assertEvalsTo("x" lineTo script(literal(10)))

		point
			.plus("y" lineTo script())
			.assertEvalsTo("y" lineTo script(literal(20)))

		point
			.plus("z" lineTo script())
			.assertEvalsToThis
	}

	@Test
	fun accessNumber() {
		script(
			"x" lineTo script(literal(10)),
			"number" lineTo script())
			.assertEvalsTo(line(literal(10)))
	}

	@Test
	fun accessText() {
		script(
			"x" lineTo script(literal("foo")),
			"text" lineTo script())
			.assertEvalsTo(line(literal("foo")))
	}

	@Test
	fun gives() {
		script(
			"number" lineTo script(),
			"gives" lineTo script("number"))
			.assertEvalsTo()
	}

	@Test
	fun givesAndAccess() {
		script(
			"number" lineTo script(),
			"gives" lineTo script("number"),
			line(literal(10)))
			.assertEvalsTo(line("number"))
	}

	@Test
	fun does() {
		script(
			"number" lineTo script(),
			"does" lineTo script("given" lineTo script()))
			.assertEvalsTo()
	}

	@Test
	fun doesAndAccess() {
		script(
			"number" lineTo script(),
			"does" lineTo script("given" lineTo script()),
			line(literal(10)))
			.assertEvalsTo("given" lineTo script(literal(10)))
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
			.assertEvalsTo(line("boolean"))

		rule
			.plus(
				"true" lineTo script(),
				"type" lineTo script())
			.assertEvalsTo(line("boolean"))

		rule
			.plus(
				"maybe" lineTo script(),
				"type" lineTo script())
			.assertEvalsTo(
				"maybe" lineTo script(),
				"type" lineTo script())
	}

	@Test
	fun anythingAppendAnything() {
		script(
			"minus" lineTo script(literal(10)),
			"append" lineTo script("minus" lineTo script(literal(20))))
			.assertEvalsTo(
				line(literal(-10)),
				line(literal(-20)))
	}

	@Test
	fun head() {
		script(
			"zero" lineTo script(),
			"plus" lineTo script("one"),
			"head" lineTo script())
			.assertEvalsTo(
				"plus" lineTo script("one"))
	}

	@Test
	fun head_empty() {
		script(
			"head" lineTo script())
			.assertEvalsToThis
	}

	@Test
	fun tail() {
		script(
			"zero" lineTo script(),
			"plus" lineTo script("one"),
			"tail" lineTo script())
			.assertEvalsTo("zero" lineTo script())
	}

	@Test
	fun tail_empty() {
		script(
			"tail" lineTo script())
			.assertEvalsToThis
	}

	@Test
	fun body() {
		script(
			"point" lineTo script(
				"x" lineTo script(literal(10)),
				"y" lineTo script(literal(20))),
			"body" lineTo script())
			.assertEvalsTo(
				"x" lineTo script(literal(10)),
				"y" lineTo script(literal(20)))
	}

	@Test
	fun body_empty() {
		script(
			"body" lineTo script())
			.assertEvalsToThis
	}

	@Test
	fun body_complex() {
		script(
			"x" lineTo script("foo"),
			"y" lineTo script("bar"),
			"body" lineTo script())
			.assertEvalsToThis
	}

	@Test
	fun function() {
		script(
			"foo" lineTo script(),
			"gives" lineTo script("bar"),
			"function" lineTo script("given"))
			.assertEvalsTo(
				context().push(pattern(program("foo")) ruleTo body(program("bar")))
					.function(program("given"))
					.scriptLine)
	}

	@Test
	fun functionApply() {
		script(
			"function" lineTo script("given"),
			"apply" lineTo script("foo"))
			.assertEvalsTo(script("given" lineTo script("foo")))
	}
}