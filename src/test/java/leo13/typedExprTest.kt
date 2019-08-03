package leo13

import leo.base.assertEqualTo
import kotlin.test.Test

class TypedExprTest {
	@Test
	fun parse() {
		val scope = scope()
		val bindings = typedExprBindings()
		val context = Context(scope, bindings)

		script()
			.typedExpr(context)
			.assertEqualTo(expr() of type())

		script("one" lineTo script())
			.typedExpr(context)
			.assertEqualTo(expr(0 lineTo expr()) of type(choice("one" lineTo type())))
	}
}