package leo25

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.script
import kotlin.test.Test

class InterpreterTest {
	@Test
	fun normalization() {
		script(
			"red" lineTo script(),
			"color" lineTo script()
		)
			.interpret
			.assertEqualTo(script("color" lineTo script("red")))
	}

	@Test
	fun struct() {
		script(
			"point" lineTo script(
				"x" lineTo script("zero"),
				"y" lineTo script("one")
			)
		)
			.interpret
			.assertEqualTo(
				script(
					"point" lineTo script(
						"x" lineTo script("zero"),
						"y" lineTo script("one")
					)
				)
			)
	}

	@Test
	fun function() {
		script("function" lineTo script("foo"))
			.interpret
			.assertEqualTo(script("function" lineTo script("native")))
	}

	@Test
	fun functionApply() {
		script(
			"function" lineTo script("given"),
			"apply" lineTo script("foo")
		)
			.interpret
			.assertEqualTo(script("given" lineTo script("foo")))
	}

	@Test
	fun define() {
		script(
			"foo" lineTo script(),
			"define" lineTo script("bar")
		)
			.interpret
			.assertEqualTo(script("foo"))
	}

	@Test
	fun defineGives() {
		script(
			"define" lineTo script(
				"foo" lineTo script(),
				"gives" lineTo script("given")
			),
			"foo" lineTo script()
		)
			.interpret
			.assertEqualTo(script("given" lineTo script("foo")))
	}

	@Test
	fun defineIs() {
		script(
			"define" lineTo script(
				"foo" lineTo script(),
				"is" lineTo script("given")
			),
			"foo" lineTo script()
		)
			.interpret
			.assertEqualTo(script("given"))
	}
}
