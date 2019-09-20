package leo13.expression

import leo.base.assertEqualTo
import leo13.expressionName
import leo13.previousName
import leo13.script.lineTo
import leo13.script.script
import kotlin.test.Test

class ExpressionTest {
	@Test
	fun scriptLine() {
		expression(previous.op)
			.scriptLine
			.assertEqualTo(expressionName lineTo script("script" lineTo script(previousName)))
	}
}
