package leo25

import leo.base.assertEqualTo
import leo14.line
import leo14.lineTo
import leo14.literal
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
			.assertEqualTo(script("red" lineTo script(), "color" lineTo script()))
	}

	@Test
	fun textPlusTest() {
		script(
			line(literal("Hello, ")),
			"plus" lineTo script(literal("world!"))
		)
			.interpret
			.assertEqualTo(script(literal("Hello, world!")))
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
			.assertEqualTo(script("function" lineTo script("foo")))
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
			letName lineTo script("bar")
		)
			.interpret
			.assertEqualTo(script("foo"))
	}

	@Test
	fun defineGives() {
		script(
			letName lineTo script(
				"foo" lineTo script(),
				giveName lineTo script("given")
			),
			"foo" lineTo script()
		)
			.interpret
			.assertEqualTo(script(givenName lineTo script("foo")))
	}

	@Test
	fun defineIs() {
		script(
			letName lineTo script(
				"foo" lineTo script(),
				isName lineTo script("given")
			),
			"foo" lineTo script()
		)
			.interpret
			.assertEqualTo(script("given"))
	}
}
