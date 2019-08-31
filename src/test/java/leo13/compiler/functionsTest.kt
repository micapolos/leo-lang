package leo13.compiler

import leo.base.assertEqualTo
import leo13.type.lineTo
import leo13.type.type
import leo13.value.expr
import leo13.value.lineTo
import leo13.value.op
import kotlin.test.Test

class FunctionsTest {
	@Test
	fun typedOrNull() {
		functions()
			.compiledOrNull(trace(type("zero" lineTo type())))
			.assertEqualTo(null)

		functions(
			function(
				trace(type("zero" lineTo type())),
				compiled(
					expr(op("one" lineTo expr())),
					trace(type("one" lineTo type())))))
			.compiledOrNull(trace(type("zero" lineTo type())))
			.assertEqualTo(
				compiled(
					expr(op("one" lineTo expr())),
					trace(type("one" lineTo type()))))
	}
}