package leo14

import leo.base.assertEqualTo
import kotlin.test.Test

class EvalTest {
	@Test
	fun evalEmpty() {
		script()
			.eval
			.assertEqualTo(script())
	}

	@Test
	fun evalStatic() {
		script(
			"vec" lineTo script(
				"x" lineTo script("zero"),
				"y" lineTo script("one")))
			.eval
			.assertEqualTo(
				script(
					"vec" lineTo script(
						"x" lineTo script("zero"),
						"y" lineTo script("one"))))
	}

	@Test
	fun evalAs() {
		script(
			"zero" lineTo script(),
			"as" lineTo script("choice" lineTo script("zero", "one")))
			.eval
			.assertEqualTo(script("zero"))
	}

	@Test
	fun evalMatchFirst() {
		script(
			"zero" lineTo script(),
			"as" lineTo script("choice" lineTo script("zero", "one")),
			"match" lineTo script(
				"zero" lineTo script(
					"foo" lineTo script(),
					"as" lineTo script("choice" lineTo script("foo", "bar"))),
				"one" lineTo script(
					"bar" lineTo script(),
					"as" lineTo script("choice" lineTo script("foo", "bar")))))
			.eval
			.assertEqualTo(script("foo"))
	}

	@Test
	fun evalMatchSecond() {
		script(
			"one" lineTo script(),
			"as" lineTo script("choice" lineTo script("zero", "one")),
			"match" lineTo script(
				"zero" lineTo script(
					"foo" lineTo script(),
					"as" lineTo script("choice" lineTo script("foo", "bar"))),
				"one" lineTo script(
					"bar" lineTo script(),
					"as" lineTo script("choice" lineTo script("foo", "bar")))))
			.eval
			.assertEqualTo(script("bar"))
	}

	@Test
	fun evalGetFirst() {
		script(
			"vec" lineTo script(
				"x" lineTo script("zero"),
				"y" lineTo script("one")),
			"x" lineTo script())
			.eval
			.assertEqualTo(script("x" lineTo script("zero")))
	}

	@Test
	fun evalGetSecond() {
		script(
			"vec" lineTo script(
				"x" lineTo script("zero"),
				"y" lineTo script("one")),
			"y" lineTo script())
			.eval
			.assertEqualTo(script("y" lineTo script("one")))
	}

	@Test
	fun evalMake() {
		script(
			"vec" lineTo script(
				"x" lineTo script("zero"),
				"y" lineTo script("one")),
			Keyword.MAKE.string lineTo script("z"))
			.eval
			.assertEqualTo(
				script(
					"z" lineTo script(
						"vec" lineTo script(
							"x" lineTo script("zero"),
							"y" lineTo script("one")))))
	}

	@Test
	fun evalFunction() {
		script(
			Keyword.FUNCTION.string lineTo script(
				"zero" lineTo script(),
				Keyword.GIVES.string lineTo script("one")))
			.eval
			.assertEqualTo(
				script(
					Keyword.FUNCTION.string lineTo script(
						"zero" lineTo script(),
						Keyword.GIVES.string lineTo script("one"))))
	}

	@Test
	fun evalFunctionApply() {
		script(
			Keyword.FUNCTION.string lineTo script(
				"zero" lineTo script(),
				Keyword.GIVES.string lineTo script("one")),
			Keyword.APPLY.string lineTo script("zero"))
			.eval
			.assertEqualTo(script("one"))
	}

	@Test
	fun rememberItIsAndRemind() {
		script(
			Keyword.DEFINE.string lineTo script(
				"zero" lineTo script(),
				Keyword.IS.string lineTo script("one")),
			"zero" lineTo script())
			.eval
			.assertEqualTo(script("one"))
	}

	@Test
	fun simulateHasUsingRememberItIs() {
		script(
			Keyword.DEFINE.string lineTo script(
				"zero" lineTo script(),
				Keyword.IS.string lineTo script(
					"zero" lineTo script(),
					Keyword.AS.string lineTo script(
						Keyword.CHOICE.string lineTo script("zero", "one")))),
			Keyword.DEFINE.string lineTo script(
				"one" lineTo script(),
				Keyword.IS.string lineTo script(
					"one" lineTo script(),
					Keyword.AS.string lineTo script(
						Keyword.CHOICE.string lineTo script("zero", "one")))),
			Keyword.DEFINE.string lineTo script(
				Keyword.CHOICE.string lineTo script("zero", "one"),
				"type" lineTo script(),
				Keyword.IS.string lineTo script(
					"bit" lineTo script())),
			"zero" lineTo script(),
			"type" lineTo script())
			.eval
			.assertEqualTo(script("bit"))
	}

	@Test
	fun evalNativeString() {
		script(line(literal("foo")))
			.eval
			.assertEqualTo(script(literal("foo")))
	}

	@Test
	fun evalNativeInt() {
		script(line(literal(2)))
			.eval
			.assertEqualTo(script(literal(2)))
	}

	@Test
	fun evalNativeDouble() {
		script(line(literal(2.0)))
			.eval
			.assertEqualTo(script(literal(2.0)))
	}

	@Test
	fun evalIntPlus() {
		script(
			line(literal(2)),
			"plus" lineTo script(literal(3)))
			.eval
			.assertEqualTo(script(literal(5)))
	}

	@Test
	fun evalDoublePlus() {
		script(
			line(literal(2.0)),
			"plus" lineTo script(literal(3.0)))
			.eval
			.assertEqualTo(script(literal(5.0)))
	}
}