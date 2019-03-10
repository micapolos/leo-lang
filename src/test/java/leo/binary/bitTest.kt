package leo.binary

import leo.base.EnumBit
import leo.base.assertEqualTo
import leo.base.int
import org.junit.Test

class BitTest {
	@Test
	fun int() {
		EnumBit.ZERO.int.assertEqualTo(0)
		EnumBit.ONE.int.assertEqualTo(1)
	}
}