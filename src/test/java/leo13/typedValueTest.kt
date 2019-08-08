package leo13

import leo.base.assertEqualTo
import kotlin.test.Test

class TypedValueTest {
	@Test
	fun plus() {
		value(0 lineTo value()).of(type("one" lineTo type()))
			.plus("plus" lineTo value(0 lineTo value()).of(type("two" lineTo type())))
			.assertEqualTo(
				value(0 lineTo value(), 0 lineTo value(0 lineTo value())).of(
					type("one" lineTo type(), "plus" lineTo type("two" lineTo type()))))
	}
}