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

	@Test
	fun eval_factorial() {
		val fn = fn(fn(fn(
			arg0<Expr>()
				.invoke(term(switchExpr(
					arg1(),
					arg1()))))))
//					arg2<Expr>()
//						.invoke(arg1<Expr>().invoke(term(timesOp.expr)).invoke(arg0()))
//						.invoke(arg0<Expr>().invoke(term(decMod.expr)))))))))
		fn
			.invoke(fn)
			.invoke(term(1.expr))
			.invoke(term(10.expr))
			.exprEval
			.assertEqualTo(term(2.expr))
	}
}