package leo13

import leo.base.assertEqualTo
import leo.base.assertFails
import org.junit.Test

class ExprTest {
	@Test
	fun eval() {
		value().expr
			.eval(value(0 lineTo value()))
			.assertEqualTo(value())

		value(0 lineTo value()).expr
			.eval(value(1 lineTo value()))
			.assertEqualTo(value(0 lineTo value()))

		expr(op(argument))
			.eval(value(0 lineTo value()))
			.assertEqualTo(value(0 lineTo value()))

		value(
				0 lineTo value(),
				1 lineTo value(
					2 lineTo value(),
					3 lineTo value()))
			.expr
			.plus(op(access(0)))
			.apply {
				eval(value()).assertEqualTo(value(3 lineTo value()))
			}

		run {
			val switchOp = switchOp(
				expr(op(argument)).plus(op(opLink(20 lineTo expr()))),
				expr(op(argument)).plus(op(opLink(10 lineTo expr()))))
			val parameter = 100 lineTo value()

			value(0 lineTo value())
				.expr
				.plus(switchOp)
				.eval(value(parameter))
				.assertEqualTo(value(parameter, 10 lineTo value()))

			value(1 lineTo value())
				.expr
				.plus(switchOp)
				.eval(value(parameter))
				.assertEqualTo(value(parameter, 20 lineTo value()))

			value(2 lineTo value())
				.expr
				.plus(switchOp)
				.assertFails { eval(value(parameter)) }
		}
	}
}