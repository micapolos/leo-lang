package leo13.type

import leo.base.assertEqualTo
import leo13.value.expr
import leo13.value.lineTo
import leo13.value.op
import kotlin.test.Test

class FunctionsTest {
	@Test
	fun typedOrNull() {
		functions()
			.typedOrNull(type("zero" lineTo type()))
			.assertEqualTo(null)

		functions(
			function(
				type("zero" lineTo type()),
				typed(
					expr(op("one" lineTo expr())),
					type("one" lineTo type()))))
			.typedOrNull(type("zero" lineTo type()))
			.assertEqualTo(
				typed(
					expr(op("one" lineTo expr())),
					type("one" lineTo type())))
	}
}