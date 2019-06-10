package leo5.core

import leo.base.assertEqualTo
import kotlin.test.Test

class IntTest {
	@Test
	fun ints() {
		value(13).int.assertEqualTo(13)
		value(-1).int.assertEqualTo(-1)
	}
}