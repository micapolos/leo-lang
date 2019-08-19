package leo13.script

import leo.base.assertEqualTo
import leo13.argument
import leo13.lineTo
import leo13.previous
import leo13.script
import kotlin.test.Test
import kotlin.test.assertFails

class ExprTest {
	@Test
	fun evalArgument() {
		expr(op(argument()))
			.eval(bindings(script("zero" lineTo script()), script("one" lineTo script())))
			.assertEqualTo(script("one" lineTo script()))

		expr(op(argument(previous)))
			.eval(bindings(script("zero" lineTo script()), script("one" lineTo script())))
			.assertEqualTo(script("zero" lineTo script()))

		assertFails {
			expr(op(argument(previous, previous)))
				.eval(bindings(script("zero" lineTo script()), script("one" lineTo script())))
		}
	}
}