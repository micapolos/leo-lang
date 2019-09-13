package leo13.untyped.expression

import leo.base.assertEqualTo
import leo13.script.lineTo
import leo13.script.script
import leo13.untyped.expressionName
import leo13.untyped.forgetName
import leo13.untyped.replaceName
import leo13.untyped.value.bodyScript
import leo13.untyped.value.value
import kotlin.test.Test

class ExpressionTest {
	@Test
	fun scriptLine() {
		expression(replace(value()).op)
			.scriptLine
			.assertEqualTo(expressionName lineTo script(forgetName))

		expression(replace(value("foo")).op)
			.scriptLine
			.assertEqualTo(expressionName lineTo script(replaceName lineTo value("foo").bodyScript))
	}
}
