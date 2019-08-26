package leo13.script

import leo.base.assertReturns
import leo13.*
import org.junit.Test

class ExprTest {
	@Test
	fun constructor() {
		expr(
			op(argument()),
			op(argument(outside, outside)),
			op(lhs),
			op(rhs),
			op(rhsLine),
			op(get("zero")),
			op(switch("zero" caseTo expr(), "one" caseTo expr())),
			op("zero" lineTo expr(op(argument()))),
			op(call(expr(op(argument())))))
			.toString()
			.assertReturns()
	}
}