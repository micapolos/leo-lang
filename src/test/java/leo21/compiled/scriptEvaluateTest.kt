package leo21.compiled

import leo.base.assertEqualTo
import leo14.line
import leo14.lineTo
import leo14.literal
import leo14.script
import kotlin.test.Test

class ScriptEvaluateTest {
	@Test
	fun empty() {
		script().evaluate.assertEqualTo(script())
	}

	@Test
	fun get() {
		script(
			"x" lineTo script(literal(10.0)),
			"number" lineTo script())
			.evaluate
			.assertEqualTo(script(literal(10.0)))
	}

	@Test
	fun numberPlusNumber() {
		script(
			line(literal(10.0)),
			"plus" lineTo script(literal(20.0)))
			.evaluate
			.assertEqualTo(script(literal(30.0)))
	}

	@Test
	fun textPlusText() {
		script(
			line(literal("Hello, ")),
			"plus" lineTo script(literal("world!")))
			.evaluate
			.assertEqualTo(script(literal("Hello, world!")))
	}
}