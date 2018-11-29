package leo.base

import kotlin.test.Test

class BitStackTest {
	@Test
	fun bitArrayBitStack() {
		bitArray(0)
			.fullBitStack
			.assertEqualTo(BitStack(bitArray(0), null))
		bitArray(0, 1)
			.fullBitStack
			.assertEqualTo(BitStack(bitArray(0, 1), binary(1)))
		bitArray(0, 1, 1, 1)
			.fullBitStack
			.assertEqualTo(BitStack(bitArray(0, 1, 1, 1), binary(1, 1)))
	}

	@Test
	fun pushBit() {

	}
}