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
	fun evalGive() {
		script(
			"zero" lineTo script(),
			"give" lineTo script("one"))
			.eval
			.assertEqualTo(script("one"))
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
			"action" lineTo script(
				"it" lineTo script("zero"),
				"does" lineTo script("plus" lineTo script("one"))))
			.eval
			.assertEqualTo(
				script(
					"action" lineTo script(
						"it" lineTo script("zero"),
						"gives" lineTo script(
							"zero" lineTo script(),
							"plus" lineTo script("one")))))
	}

	@Test
	fun evalFunctionDo() {
		script(
			"action" lineTo script(
				"it" lineTo script("zero"),
				"does" lineTo script("plus" lineTo script("one"))),
			"do" lineTo script("zero"))
			.eval
			.assertEqualTo(
				script(
					"zero" lineTo script(),
					"plus" lineTo script("one")))
	}
}