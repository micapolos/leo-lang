package leo.base

import kotlin.test.Test

class BitCountTest {
	@Test
	fun primitiveBitCount() {
		bitBitCount.intOrNull.assertEqualTo(1)
		byteBitCount.intOrNull.assertEqualTo(8)
		shortBitCount.intOrNull.assertEqualTo(16)
		intBitCount.intOrNull.assertEqualTo(32)
		longBitCount.intOrNull.assertEqualTo(64)
		floatBitCount.intOrNull.assertEqualTo(32)
		doubleBitCount.intOrNull.assertEqualTo(64)
	}
}