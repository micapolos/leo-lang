package leo21.evaluator

import leo.base.assertEqualTo
import leo14.line
import leo14.lineTo
import leo14.literal
import leo14.script
import kotlin.test.Test

class ScriptEvaluateTest {
	@Test
	fun numberPlus() {
		script(
			line(literal(10.0)),
			"plus" lineTo script(literal(20.0)),
			"times" lineTo script(literal(30.0)))
			.evaluate
			.assertEqualTo(script(literal(900.0)))
	}
}