package leo5

import leo.base.assertEqualTo
import kotlin.test.Test

class ValueTest {
	@Test
	fun constructor() {
		val zero = script(line("zero")) of self
		val one = script(line("one")) of self

		val bit = script(line("bit")) of self
		val bitZero = script(line("bit", zero)) of bit
		val bitOne = script(line("bit", one)) of bit

		bitZero.typeValue.assertEqualTo(bit)
		bitOne.typeValue.assertEqualTo(bit)

		val bitSelfFn = script(
			function(self)
				.rhs
				.dispatch(
					"zero" to function(bitOne),
					"one" to function(bitZero))) of type(returns(bit))
		bitSelfFn.invoke(bitOne).assertEqualTo(bitZero)
	}
}
