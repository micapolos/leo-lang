package leo13.script

import leo.base.assertReturns
import leo13.*
import leo13.value.expr
import leo13.value.lineTo
import leo13.value.op
import org.junit.Test

class ExprTest {
	@Test
	fun constructor() {
		expr(
			op(given()),
			op(given(outside, outside)),
			op(lhs),
			op(rhs),
			op(rhsLine),
			op(get("zero")),
			op(switch("zero" caseTo expr(), "one" caseTo expr())),
			op("zero" lineTo expr(op(given()))),
			op(call(expr(op(given())))))
			.toString()
			.assertReturns()
	}
}