package leo13.value

import leo.base.assertReturns
import leo13.lhs
import leo13.rhs
import leo13.rhsLine
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