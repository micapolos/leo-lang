package leo.base

import kotlin.test.Test

class BitCountTest {
	@Test
	fun primitiveBitCount() {
		byteBitCount.assertEqualTo(8.bitCount)
		shortBitCount.assertEqualTo(16.bitCount)
		intBitCount.assertEqualTo(32.bitCount)
		longBitCount.assertEqualTo(64.bitCount)
		floatBitCount.assertEqualTo(32.bitCount)
		doubleBitCount.assertEqualTo(64.bitCount)
	}
}