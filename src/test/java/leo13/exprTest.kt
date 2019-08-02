package leo13

import leo.base.assertEqualTo
import org.junit.Test

class ExprTest {
	@Test
	fun eval() {
		expr(value())
			.eval(value(0 lineTo value()))
			.assertEqualTo(value())

		expr(value(0 lineTo value()))
			.eval(value(1 lineTo value()))
			.assertEqualTo(value(0 lineTo value()))

		expr(argument)
			.eval(value(0 lineTo value()))
			.assertEqualTo(value(0 lineTo value()))

		expr(
			exec(
				expr(
					value(
						0 lineTo value(),
						1 lineTo value(
							2 lineTo value(),
							3 lineTo value()))),
				op(get(0))))
			.apply {
				eval(value()).assertEqualTo(value(3 lineTo value()))
			}
	}
}