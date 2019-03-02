package leo.binary

import leo.base.Bit
import leo.base.assertEqualTo
import kotlin.test.Test

class BinaryTest {
	@Test
	fun bitsBinary() {
		binary(Bit.ZERO).assertEqualTo(Binary(Bit.ZERO, null))
		binary(Bit.ZERO, Bit.ONE).assertEqualTo(Binary(Bit.ZERO, Binary(Bit.ONE, null)))
		binary(Bit.ZERO, Bit.ONE, Bit.ONE).assertEqualTo(Binary(Bit.ZERO, Binary(Bit.ONE, Binary(Bit.ONE, null))))
	}
}