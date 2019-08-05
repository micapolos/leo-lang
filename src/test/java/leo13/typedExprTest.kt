package leo13

import leo.base.assertEqualTo
import kotlin.test.Test

class TypedExprTest {
	@Test
	fun parse() {
		val types = types()
		val scope = scope(
			function(
				parameter(type("foo" lineTo type())),
				expr(0 lineTo expr()) of type("bar" lineTo type())))
		val bindings = typedExprBindings(
			expr(0 lineTo expr()) of type(choice("binding" lineTo type())))
		val context = Context(types, scope, bindings)

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

		script(
			"vec" lineTo script(
				"x" lineTo script(
					"one" lineTo script()),
				"y" lineTo script(
					"two" lineTo script())))
			.apply {
				plus("x" lineTo script())
					.typedExpr(context)
					.assertEqualTo(
						expr(0 lineTo expr(
							0 lineTo expr(
								0 lineTo expr()),
							0 lineTo expr(
								0 lineTo expr())))
							.plus(op(access(1)))
							.of(type("x" lineTo type("one" lineTo type()))))

				plus("y" lineTo script())
					.typedExpr(context)
					.assertEqualTo(
						expr(0 lineTo expr(
							0 lineTo expr(
								0 lineTo expr()),
							0 lineTo expr(
								0 lineTo expr())))
							.plus(op(access(0)))
							.of(type("y" lineTo type("two" lineTo type()))))
			}

		script("foo" lineTo script())
			.typedExpr(context)
			.assertEqualTo(
				expr(0 lineTo expr())
					.plus(op(call(expr(0 lineTo expr()))))
					.of(type("bar" lineTo type())))
	}

	@Test
	fun accessOrNull() {
		expr(128 lineTo expr())
			.of(type(
				"vec" lineTo type(
					"x" lineTo type(
						"one" lineTo type()),
					"y" lineTo type(
						"two" lineTo type()))))
			.apply {
				accessOrNull("x").assertEqualTo(
					expr(128 lineTo expr())
						.plus(op(access(1)))
						.of(type("x" lineTo type("one" lineTo type()))))

				accessOrNull("y").assertEqualTo(
					expr(128 lineTo expr())
						.plus(op(access(0)))
						.of(type("y" lineTo type("two" lineTo type()))))

				accessOrNull("z").assertEqualTo(null)
			}
	}
}