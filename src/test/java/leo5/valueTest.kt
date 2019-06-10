package leo5

import leo.base.assertEqualTo
import leo.base.empty
import kotlin.test.Test

class ValueTest {
	@Test
	fun constructor() {
		val zero = value(script(line("zero")))
		val one = value(script(line("one")))

		val bitZero = value(script(line("bit", zero)))
		val bitOne = value(script(line("bit", one)))

		val bitSelfFn = value(
			function(
				pattern(empty),
				body(self)
					.rhs
					.dispatch(
						"zero" to body(bitOne),
						"one" to body(bitZero))))
		bitSelfFn.invoke(bitOne).assertEqualTo(bitZero)
	}
}
