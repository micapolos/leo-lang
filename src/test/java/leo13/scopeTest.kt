package leo13

import leo.base.assertEqualTo
import kotlin.test.Test

class ScopeTest {
	@Test
	fun evalTypedScript() {
		val enType = type(choice("two" lineTo type(), "one" lineTo type()))
		val plType = type(choice("dwa" lineTo type(), "jeden" lineTo type()))
		val deType = type(choice("zwei" lineTo type(), "eins" lineTo type()))

		val scope = scope(
			function(
				parameter(enType),
				expr(op(argument())) of plType),
			function(
				parameter(plType),
				expr(op(argument())) of deType))

		scope
			.eval(script("one" lineTo script()) of enType)
			.assertEqualTo(script("jeden" lineTo script()) of plType)

		scope
			.eval(script("two" lineTo script()) of enType)
			.assertEqualTo(script("dwa" lineTo script()) of plType)

		scope
			.eval(script("jeden" lineTo script()) of plType)
			.assertEqualTo(script("eins" lineTo script()) of deType)

		scope
			.eval(script("dwa" lineTo script()) of plType)
			.assertEqualTo(script("zwei" lineTo script()) of deType)
	}
}