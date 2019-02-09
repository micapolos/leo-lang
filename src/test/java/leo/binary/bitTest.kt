package leo.binary

import leo.base.assertEqualTo
import org.junit.Test

class BitTest {
	@Test
	fun int() {
		Bit0.int.assertEqualTo(0)
		Bit1.int.assertEqualTo(1)
	}
}