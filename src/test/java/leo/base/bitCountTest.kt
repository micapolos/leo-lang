package leo.base

import kotlin.test.Test

class BitCountTest {
	@Test
	fun intBitCount() {
		(-1).bitCountOrNull.assertEqualTo(null)
		0.bitCountOrNull.assertEqualTo(null)
		1.bitCountOrNull?.int.assertEqualTo(1)
		64.bitCountOrNull?.int.assertEqualTo(64)
		maxInt.bitCountOrNull?.int.assertEqualTo(maxInt)
	}

	@Test
	fun primitiveBitCount() {
		bitBitCount.int.assertEqualTo(1)
		byteBitCount.int.assertEqualTo(8)
		shortBitCount.int.assertEqualTo(16)
		intBitCount.int.assertEqualTo(32)
		longBitCount.int.assertEqualTo(64)
		floatBitCount.int.assertEqualTo(32)
		doubleBitCount.int.assertEqualTo(64)
	}
}