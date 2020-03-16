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
	fun thisIsThat() {
		script(
			"x" lineTo script(),
			"is" lineTo script(literal(10)))
			.assertEvalsTo()
	}

	@Test
	fun thisIsThatAndAccess() {
		script(
			"x" lineTo script(),
			"is" lineTo script(literal(10)),
			"x" lineTo script())
			.assertEvalsTo(line(literal(10)))
	}

	@Test
	fun thisAsThat() {
		script(
			line(literal(10)),
			"as" lineTo script("foo"))
			.assertEvalsTo()
	}

	@Test
	fun thisAsThatAndAccess() {
		script(
			line(literal(10)),
			"as" lineTo script("foo"),
			"foo" lineTo script())
			.assertEvalsTo(line(literal(10)))
	}

	@Test
	fun thisGivesThat() {
		script(
			"number" lineTo script(),
			"does" lineTo script("given" lineTo script()))
			.assertEvalsTo()
	}

	@Test
	fun thisGivesThatAndAccess() {
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
			"does" lineTo script("boolean"))

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
	fun contents() {
		script(
			"point" lineTo script(
				"x" lineTo script(literal(10)),
				"y" lineTo script(literal(20))),
			"contents" lineTo script())
			.assertEvalsTo(
				"x" lineTo script(literal(10)),
				"y" lineTo script(literal(20)))
	}

	@Test
	fun contents_empty() {
		script(
			"contents" lineTo script())
			.assertEvalsToThis
	}

	@Test
	fun contents_complex() {
		script(
			"x" lineTo script("foo"),
			"y" lineTo script("bar"),
			"contents" lineTo script())
			.assertEvalsToThis
	}

	@Test
	fun function() {
		script(
			"foo" lineTo script(),
			"is" lineTo script("bar"),
			"function" lineTo script(
				"zoo" lineTo script(),
				"meta" lineTo script("is" lineTo script("zar")),
				"append" lineTo script("foo")))
			.assertEvalsTo(
				"function" lineTo script(
					"context" lineTo script(
						"foo" lineTo script(),
						"is" lineTo script("bar")),
					"body" lineTo script(
						"zoo" lineTo script(),
						"is" lineTo script("zar"),
						"bar" lineTo script())))
	}

	@Test
	fun functionApply() {
		script(
			"function" lineTo script("given"),
			"apply" lineTo script("foo"))
			.assertEvalsTo(script("given" lineTo script("foo")))
	}

	@Test
	fun meta() {
		script(
			line(literal(10)),
			"meta" lineTo script(
				"plus" lineTo script(literal(20))))
			.assertEvalsTo(
				script(
					line(literal(10)),
					"plus" lineTo script(literal(20))))
	}

	@Test
	fun metaGives() {
		script(
			"foo" lineTo script(),
			"meta" lineTo script(
				"gives" lineTo script("bar")))
			.assertEvalsTo(
				"foo" lineTo script(),
				"gives" lineTo script("bar"))
	}

	@Test
	fun metaMeta() {
		script(
			line(literal(10)),
			"meta" lineTo script(
				"meta" lineTo script(
					"plus" lineTo script(literal(20)))))
			.assertEvalsTo(
				script(
					line(literal(10)),
					"meta" lineTo script(
						"plus" lineTo script(literal(20)))))
	}

	@Test
	fun equals() {
		script(
			"foo" lineTo script(),
			"equals" lineTo script("foo"))
			.assertEvalsTo(script("yes"))

		script(
			"foo" lineTo script(),
			"equals" lineTo script("bar"))
			.assertEvalsTo(script("no"))
	}

	@Test
	fun ifThenElse_yes() {
		script(
			"if" lineTo script("yes"),
			"then" lineTo script("hurray"),
			"else" lineTo script("booo"))
			.assertEvalsTo(script("hurray"))
	}

	@Test
	fun ifThenElse_no() {
		script(
			"if" lineTo script("no"),
			"then" lineTo script("hurray"),
			"else" lineTo script("booo"))
			.assertEvalsTo(script("booo"))
	}

	@Test
	fun ifThenElse_other() {
		script(
			"if" lineTo script("other"),
			"then" lineTo script("hurray"),
			"else" lineTo script("booo"))
			.assertEvalsToThis
	}
}