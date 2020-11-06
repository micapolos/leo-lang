package leo21.token.processor

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.literal
import leo14.script
import kotlin.test.Test

class EvaluateTest {
	@Test
	fun number() {
		script(literal(10.0))
			.evaluate
			.assertEqualTo(script(literal(10.0)))
	}

	@Test
	fun text() {
		script(literal("Hello, world!"))
			.evaluate
			.assertEqualTo(script(literal("Hello, world!")))
	}

	@Test
	fun struct() {
		script(
			"x" lineTo script(literal(10.0)),
			"y" lineTo script(literal(20.0)))
			.evaluate
			.assertEqualTo(
				script(
					"x" lineTo script(literal(10.0)),
					"y" lineTo script(literal(20.0))))
	}

	@Test
	fun get() {
		script(
			"point" lineTo script(
				"x" lineTo script(literal(10.0)),
				"y" lineTo script(literal(20.0))),
			"x" lineTo script())
			.evaluate
			.assertEqualTo(script("x" lineTo script(literal(10.0))))
	}

	@Test
	fun do_() {
		script(
			"x" lineTo script(literal(10.0)),
			"do" lineTo script("given"))
			.evaluate
			.assertEqualTo(
				script(
					"given" lineTo script(
						"x" lineTo script(literal(10.0)))))
	}
}