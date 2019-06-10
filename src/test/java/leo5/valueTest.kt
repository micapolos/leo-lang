package leo5

import leo.base.assertEqualTo
import leo.base.empty
import kotlin.test.Test

class ValueTest {
	@Test
	fun constructor() {
		val zero = value("zero" lineTo value())
		val one = value("one" lineTo value())

		val bitZero = value("bit" lineTo zero)
		val bitOne = value("bit" lineTo one)

		val bitSelfFn = value(
			function(
				pattern(empty),
				body(argument)
					.rhs
					.dispatch(
						"zero" lineTo body(bitOne),
						"one" lineTo body(bitZero))))
		bitSelfFn.invoke(parameter(bitOne)).assertEqualTo(bitZero)
	}
}
