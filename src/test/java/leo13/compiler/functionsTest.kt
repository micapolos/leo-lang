package leo13.compiler

import leo.base.assertEqualTo
import leo13.expression.apply
import leo13.expression.expression
import leo13.expression.op
import leo13.expression.valueContext
import leo13.type.arrowTo
import leo13.type.type
import leo13.value.function
import leo13.value.item
import leo13.value.value
import kotlin.test.Test

class FunctionsTest {
	@Test
	fun resolve() {
		functions()
			.plus(
				typed(
					function(
						valueContext(),
						expression("bar")),
					type("foo") arrowTo type("bar")))
			.resolve(
				typed(
					expression("foo"),
					type("foo")))
			.assertEqualTo(
				typed(
					expression(
						op(
							value(
								item(
									function(
										valueContext(),
										expression("bar"))))),
						op(apply(expression("foo")))),
					type("bar")))
	}
}