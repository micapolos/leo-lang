package leo13.compiler

import leo.base.assertEqualTo
import leo13.type.lineTo
import leo13.type.type
import leo13.value.expr
import leo13.value.value
import kotlin.test.Test

class FunctionsTest {
	@Test
	fun typedOrNull() {
		functions()
			.compiledOrNull(type("zero" lineTo type()))
			.assertEqualTo(null)

		functions(
			function(
				type("zero" lineTo type()),
				compiled(
					expr(value("one")),
					type("one" lineTo type()))))
			.compiledOrNull(type("zero" lineTo type()))
			.assertEqualTo(
				compiled(
					expr(value("one")),
					type("one" lineTo type())))
	}
}