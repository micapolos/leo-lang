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
}