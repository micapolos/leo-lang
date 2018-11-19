package leo.lab

import leo.base.Bit
import leo.base.assertEqualTo
import kotlin.test.Test

class FunctionTest {
	@Test
	fun get() {
		emptyFunction.get(Bit.ZERO).assertEqualTo(null)
	}
}