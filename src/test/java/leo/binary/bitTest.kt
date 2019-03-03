package leo.binary

import leo.base.assertEqualTo
import org.junit.Test

class BitTest {
	@Test
	fun int() {
		Bit.ZERO.int.assertEqualTo(0)
		Bit.ONE.int.assertEqualTo(1)
	}
}