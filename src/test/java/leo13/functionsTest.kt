package leo13

import leo.base.assertEqualTo
import kotlin.test.Test

class FunctionsTest {
	@Test
	fun evalTypedScript() {
		val enType = type(choice("two" lineTo type(), "one" lineTo type()))
		val plType = type(choice("dwa" lineTo type(), "jeden" lineTo type()))
		val deType = type(choice("zwei" lineTo type(), "eins" lineTo type()))

		val functions = functions(
			function(
				parameter(enType),
				expr(op(argument())) of plType),
			function(
				parameter(plType),
				expr(op(argument())) of deType))

		functions
			.eval(script("one" lineTo script()) of enType)
			.assertEqualTo(script("jeden" lineTo script()) of plType)

		functions
			.eval(script("two" lineTo script()) of enType)
			.assertEqualTo(script("dwa" lineTo script()) of plType)

		functions
			.eval(script("jeden" lineTo script()) of plType)
			.assertEqualTo(script("eins" lineTo script()) of deType)

		functions
			.eval(script("dwa" lineTo script()) of plType)
			.assertEqualTo(script("zwei" lineTo script()) of deType)
	}
}