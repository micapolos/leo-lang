package leo13

import leo.base.assertEqualTo
import kotlin.test.Test
import kotlin.test.assertFails

class ValueTest {
	@Test
	fun get() {
		value(
			1 lineTo value(),
			2 lineTo value(
				3 lineTo value(),
				4 lineTo value()))
			.apply {
				get(0).assertEqualTo(value(4 lineTo value()))
				get(1).assertEqualTo(value(3 lineTo value()))
				assertFails { get(2) }
			}

		assertFails { value().get(0) }
		assertFails { value(1 lineTo value()).get(0) }
		assertFails { value(1 lineTo value(), 2 lineTo value()).get(0) }
	}
}