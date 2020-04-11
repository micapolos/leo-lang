package leo14.untyped.typed.lambda

import leo.base.assertEqualTo
import leo14.lambda2.Thunk
import leo14.lambda2.eval
import leo14.lambda2.invoke
import leo14.lambda2.valueTerm
import kotlin.test.Test

class ExpressionTest {
	@Test
	fun apply() {
		intOrIntExpression.valueTerm
			.invoke(12.expression.valueTerm)
			.invoke(1.expression.valueTerm)
			.eval(Thunk::expressionApply)
			.assertEqualTo(13.expression.valueTerm)
	}
}