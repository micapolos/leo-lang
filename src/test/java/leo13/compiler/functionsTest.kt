package leo13.compiler

import leo.base.assertEqualTo
import leo13.expression.apply
import leo13.expression.expression
import leo13.expression.op
import leo13.expression.valueContext
import leo13.pattern.arrowTo
import leo13.pattern.pattern
import leo13.value.function
import leo13.value.item
import leo13.value.value
import kotlin.test.Test

class FunctionsTest {
	@Test
	fun resolve() {
		functions()
			.plus(
				compiled(
					function(
						valueContext(),
						expression("bar")),
					pattern("foo") arrowTo pattern("bar")))
			.resolve(
				compiled(
					expression("foo"),
					pattern("foo")))
			.assertEqualTo(
				compiled(
					expression(
						op(
							value(
								item(
									function(
										valueContext(),
										expression("bar"))))),
						op(apply(expression("foo")))),
					pattern("bar")))
	}
}