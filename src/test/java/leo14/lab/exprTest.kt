package leo14.lab

import leo.base.assertEqualTo
import leo14.lambda.*
import kotlin.test.Test

class ExprTest {
	@Test
	fun eval_inc() {
		term(0.expr)
			.invoke(term(incMod.expr))
			.exprEval
			.assertEqualTo(term(1.expr))
	}

	@Test
	fun eval_minus() {
		term(5.expr)
			.invoke(term(minusOp.expr).invoke(term(3.expr)))
			.exprEval
			.assertEqualTo(term(2.expr))
	}
}