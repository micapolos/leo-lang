package leo14.untyped

import leo.base.assertEqualTo
import leo14.literal
import org.junit.Test

class ValueTest {
	@Test
	fun all() {
		program(
			value(literal(10)),
			plusName valueTo program(literal(10)))
			.assertEqualTo(
				program(
					value(literal(10)),
					plusName valueTo program(literal(10))))
	}
}