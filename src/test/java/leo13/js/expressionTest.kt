package leo13.js

import leo.base.assertEqualTo
import org.junit.Test

class ExpressionTest {
	@Test
	fun code() {
		nullExpression.code.assertEqualTo("null")
		expression(123.0).code.assertEqualTo("(123.0)")
		expression("jajko").code.assertEqualTo("\'jajko\'")
		expression(expression(bound(0)).bindIn(expression(bound(1)))).code
			.assertEqualTo("bind(bound(0), function() { return bound(1); })")
		expression(expression(bound(0)).apply(expression(bound(1)))).code
			.assertEqualTo("bound(0)(bound(1))")
		expression(bound(0)).code.assertEqualTo("bound(0)")
		expression(expression(bound(0)).get("foo")).code.assertEqualTo("bound(0).foo")
		expression(expression(bound(0)).set("foo", expression(bound(1)))).code
			.assertEqualTo("function() { bound(0).foo = bound(1); return null; }()")
	}
}