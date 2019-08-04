package leo13

import leo.base.assertEqualTo
import leo.base.assertFails
import org.junit.Test

class ExprTest {
	@Test
	fun eval() {
		value().expr
			.eval(bindings(value(0 lineTo value())))
			.assertEqualTo(value())

		value(0 lineTo value()).expr
			.eval(bindings(value(1 lineTo value())))
			.assertEqualTo(value(0 lineTo value()))

		expr(op(argument()))
			.eval(bindings(value(0 lineTo value())))
			.assertEqualTo(value(0 lineTo value()))

		value(
				0 lineTo value(),
				1 lineTo value(
					2 lineTo value(),
					3 lineTo value()))
			.expr
			.plus(op(access(0)))
			.apply {
				eval(valueBindings()).assertEqualTo(value(3 lineTo value()))
			}

		run {
			val switchOp = switchOp(
				expr(op(argument()), op(opLink(20 lineTo expr()))),
				expr(op(argument()), op(opLink(10 lineTo expr()))))
			value(0 lineTo value(30 lineTo value()))
				.expr
				.plus(switchOp)
				.eval(valueBindings())
				.assertEqualTo(value(30 lineTo value(), 10 lineTo value()))

			value(1 lineTo value(30 lineTo value()))
				.expr
				.plus(switchOp)
				.eval(valueBindings())
				.assertEqualTo(value(30 lineTo value(), 20 lineTo value()))

			value(2 lineTo value())
				.expr
				.plus(switchOp)
				.assertFails { eval(valueBindings()) }
		}
	}

	@Test
	fun fromType() {
		type()
			.exprOrNull
			.assertEqualTo(expr())

		type("one" lineTo type())
			.exprOrNull
			.assertEqualTo(expr(0 lineTo expr()))

		type("one" lineTo type("two" lineTo type()))
			.exprOrNull
			.assertEqualTo(expr(0 lineTo expr(0 lineTo expr())))

		type("one" lineTo type(), "two" lineTo type())
			.exprOrNull
			.assertEqualTo(expr(0 lineTo expr(), 0 lineTo expr()))

		type(choice("one" lineTo type(), "two" lineTo type()))
			.exprOrNull
			.assertEqualTo(null)
	}
}