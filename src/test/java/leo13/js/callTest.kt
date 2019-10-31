package leo13.js

import leo.base.assertEqualTo
import org.junit.Test

class CallTest {
	@Test
	fun nullLhs() {
		nullExpression
			.call("foo")
			.code
			.assertEqualTo("foo()")

		nullExpression
			.call("plus", nullExpression, expression(bound(2)))
			.code
			.assertEqualTo("plus(null, bound(2))")
	}

	@Test
	fun nonNullLhs() {
		expression(bound(0))
			.call("foo")
			.code
			.assertEqualTo("bound(0).foo()")

		expression(bound(0))
			.call("plus", nullExpression, expression(bound(2)))
			.code
			.assertEqualTo("bound(0).plus(null, bound(2))")
	}
}