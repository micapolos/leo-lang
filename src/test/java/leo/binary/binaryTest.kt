package leo.binary

import leo.base.assertEqualTo
import kotlin.test.Test

class BinaryTest {
	@Test
	fun bitsBinary() {
		binary(Bit0).assertEqualTo(Binary(Bit0, null))
		binary(Bit0, Bit1).assertEqualTo(Binary(Bit0, Binary(Bit1, null)))
		binary(Bit0, Bit1, Bit1).assertEqualTo(Binary(Bit0, Binary(Bit1, Binary(Bit1, null))))
	}
}