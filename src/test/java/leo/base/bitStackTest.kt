package leo.base

import kotlin.test.Test

class BitStackTest {
	@Test
	fun bitArrayBitStack() {
		bitArray(0.bit)
			.fullBitStack
			.assertEqualTo(BitStack(bitArray(0.bit), null))
		bitArray(0.bit, 1.bit)
			.fullBitStack
			.assertEqualTo(BitStack(bitArray(0.bit, 1.bit), binary(1.bit)))
		bitArray(0.bit, 1.bit, 1.bit, 1.bit)
			.fullBitStack
			.assertEqualTo(BitStack(bitArray(0.bit, 1.bit, 1.bit, 1.bit), binary(1.bit, 1.bit)))
	}

	@Test
	fun pushBit() {

	}
}