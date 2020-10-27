package leo20

import leo.base.assertEqualTo
import kotlin.test.Test

class ValueTest {
	@Test
	fun unsafeNumberPlus() {
		value(line(2))
			.unsafeNumberPlus(value(line(3)))
			.assertEqualTo(value(line(5)))
	}
}