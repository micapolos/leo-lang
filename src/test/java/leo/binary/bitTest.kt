package leo.binary

import leo.base.Bit
import leo.base.assertEqualTo
import leo.base.int
import org.junit.Test

class BitTest {
	@Test
	fun int() {
		Bit.ZERO.int.assertEqualTo(0)
		Bit.ONE.int.assertEqualTo(1)
	}
}