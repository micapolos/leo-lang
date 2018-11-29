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
			.assertEqualTo(BitStack(bitArray(0.bit, 1.bit), binary(1)))
		bitArray(0.bit, 1.bit, 1.bit, 1.bit)
			.fullBitStack
			.assertEqualTo(BitStack(bitArray(0.bit, 1.bit, 1.bit, 1.bit), binary(1, 1)))
	}

	@Test
	fun pushBit() {

	}
}