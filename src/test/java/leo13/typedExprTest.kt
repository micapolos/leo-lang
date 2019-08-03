package leo13

import leo.base.assertEqualTo
import kotlin.test.Test

class TypedExprTest {
	@Test
	fun parse() {
		val scope = scope()
		val bindings = typedExprBindings(
			expr(0 lineTo expr()) of type(choice("binding" lineTo type())))
		val context = Context(scope, bindings)

		script()
			.typedExpr(context)
			.assertEqualTo(expr() of type())

		script("one" lineTo script())
			.typedExpr(context)
			.assertEqualTo(expr(0 lineTo expr()) of type(choice("one" lineTo type())))

		script("given" lineTo script())
			.typedExpr(context)
			.assertEqualTo(expr(0 lineTo expr()) of type(choice("binding" lineTo type())))

		script(
			"increment" lineTo script(
				"given" lineTo script()))
			.typedExpr(context)
			.assertEqualTo(
				expr(0 lineTo expr(0 lineTo expr())) of
					type(
						choice("increment" lineTo type(
							choice("binding" lineTo type())))))

		script(
			"given" lineTo script(),
			"plus" lineTo script(
				"one" lineTo script()))
			.typedExpr(context)
			.assertEqualTo(
				expr(
					0 lineTo expr(),
					0 lineTo expr(0 lineTo expr())) of
					type(
						choice("binding" lineTo type()),
						choice("plus" lineTo type(
							choice("one" lineTo type())))))
	}
}