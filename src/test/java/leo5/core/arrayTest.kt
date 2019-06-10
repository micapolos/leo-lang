package leo5.core

import leo.base.assertEqualTo
import kotlin.test.Test

class ArrayTest {
	@Test
	fun arrays() {
		hsbMaskArrayValue(0b10) { value(it) }.apply {
			hsbMaskArrayAt(0b10, 0).assertEqualTo(value(0))
			hsbMaskArrayAt(0b10, 1).assertEqualTo(value(1))
			hsbMaskArrayAt(0b10, 2).assertEqualTo(value(2))
			hsbMaskArrayAt(0b10, 3).assertEqualTo(value(3))
		}
	}
}
