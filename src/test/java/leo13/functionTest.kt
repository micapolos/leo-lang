package leo13

import leo.base.assertEqualTo
import kotlin.test.Test

class FunctionTest {
	@Test
	fun parse() {
		val scope = scope()
		val bindings = typedExprBindings()
		val context = context(scope, bindings)

		script(
			"one" lineTo script(),
			"gives" lineTo script(
				"two" lineTo script()))
			.functionOrNull(context)
			.assertEqualTo(
				function(
					parameter(type(choice("one" lineTo type()))),
					expr(0 lineTo expr()) of type(choice("two" lineTo type()))))
	}
}