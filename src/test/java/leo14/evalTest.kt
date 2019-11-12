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
	fun evalDelete() {
		script("zero", "delete")
			.eval
			.assertEqualTo(script())
	}

	@Test
	fun evalOf() {
		script(
			"zero" lineTo script(),
			"of" lineTo script("choice" lineTo script("zero", "one")))
			.eval
			.assertEqualTo(script("zero"))
	}

	@Test
	fun evalMatchFirst() {
		script(
			"zero" lineTo script(),
			"of" lineTo script("choice" lineTo script("zero", "one")),
			"match" lineTo script(
				"zero" lineTo script(
					"foo" lineTo script(),
					"of" lineTo script("choice" lineTo script("foo", "bar"))),
				"one" lineTo script(
					"bar" lineTo script(),
					"of" lineTo script("choice" lineTo script("foo", "bar")))))
			.eval
			.assertEqualTo(script("foo"))
	}

	@Test
	fun evalMatchSecond() {
		script(
			"one" lineTo script(),
			"of" lineTo script("choice" lineTo script("zero", "one")),
			"match" lineTo script(
				"zero" lineTo script(
					"foo" lineTo script(),
					"of" lineTo script("choice" lineTo script("foo", "bar"))),
				"one" lineTo script(
					"bar" lineTo script(),
					"of" lineTo script("choice" lineTo script("foo", "bar")))))
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
	fun evalWrap() {
		script(
			"vec" lineTo script(
				"x" lineTo script("zero"),
				"y" lineTo script("one")),
			"z" lineTo script())
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
			"any" lineTo script("zero"),
			"does" lineTo script("plus" lineTo script("one")))
			.eval
			.assertEqualTo(
				script(
					"function" lineTo script(
						"from" lineTo script("zero"),
						"to" lineTo script(
							"zero" lineTo script(),
							"plus" lineTo script("one")))))
	}

	@Test
	fun evalFunctionApply() {
		script(
			"any" lineTo script("zero"),
			"does" lineTo script("plus" lineTo script("one")),
			"apply" lineTo script("zero"))
			.eval
			.assertEqualTo(
				script(
					"zero" lineTo script(),
					"plus" lineTo script("one")))
	}
}