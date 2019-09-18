package leo13.untyped.expression

import leo.base.assertEqualTo
import leo13.script.lineTo
import leo13.script.script
import leo13.untyped.expressionName
import leo13.untyped.previousName
import kotlin.test.Test

class ExpressionTest {
	@Test
	fun scriptLine() {
		expression(previous.op)
			.scriptLine
			.assertEqualTo(expressionName lineTo script("script" lineTo script(previousName)))
	}
}
