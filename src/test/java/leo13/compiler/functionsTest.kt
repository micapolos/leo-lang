package leo13.compiler

import leo.base.assertEqualTo
import leo13.type.lineTo
import leo13.type.pattern
import leo13.type.type
import leo13.value.expr
import leo13.value.lineTo
import leo13.value.op
import kotlin.test.Test

class FunctionsTest {
	@Test
	fun typedOrNull() {
		functions()
			.compiledOrNull(type(pattern("zero" lineTo pattern())))
			.assertEqualTo(null)

		functions(
			function(
				type(pattern("zero" lineTo pattern())),
				compiled(
					expr(op("one" lineTo expr())),
					type(pattern("one" lineTo pattern())))))
			.compiledOrNull(type(pattern("zero" lineTo pattern())))
			.assertEqualTo(
				compiled(
					expr(op("one" lineTo expr())),
					type(pattern("one" lineTo pattern()))))
	}
}