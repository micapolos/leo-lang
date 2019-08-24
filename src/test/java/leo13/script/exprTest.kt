package leo13.script

import leo.base.assertEqualTo
import leo13.argument
import leo13.lineTo
import leo13.script
import kotlin.test.Test

class ExprTest {
	@Test
	fun evalArgument() {
		expr(op(argument()))
			.eval(bindings(script("one" lineTo script())))
			.assertEqualTo(script("one" lineTo script()))
	}

	@Test
	fun evalSwitch() {
		expr(
			op("one" lineTo expr(op("rhs" lineTo expr()))),
			op(
				switch(
					"one" caseTo expr(op("jeden" lineTo expr())),
					"two" caseTo expr(op("dwa" lineTo expr())))))
			.eval(bindings())
			.assertEqualTo(
				script(
					"rhs" lineTo script(),
					"jeden" lineTo script()))
	}
}