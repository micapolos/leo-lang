package leo.binary

import leo.base.assertEqualTo
import kotlin.test.Test

class BinaryTest {
	@Test
	fun bitsBinary() {
		binary(zero.bit).assertEqualTo(Binary(zero.bit, null))
		binary(zero.bit, one.bit).assertEqualTo(Binary(zero.bit, Binary(one.bit, null)))
		binary(zero.bit, one.bit, one.bit).assertEqualTo(Binary(zero.bit, Binary(one.bit, Binary(one.bit, null))))
	}
}