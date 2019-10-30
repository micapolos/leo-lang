package leo13.js

import leo.base.assertEqualTo
import org.junit.Test

class CallTest {
	@Test
	fun code() {
		expression(bound(0))
			.call("foo")
			.code
			.assertEqualTo("bound(0).foo()")

		expression(bound(0))
			.call("plus", expression(bound(1)), expression(bound(2)))
			.code
			.assertEqualTo("bound(0).plus(bound(1), bound(2))")
	}
}