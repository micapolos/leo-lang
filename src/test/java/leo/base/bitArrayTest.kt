package leo.base

import kotlin.test.Test

class BitArrayTest {
	@Test
	fun string() {
		13.clampedByte
			.bitArray
			.string
			.assertEqualTo("[00001101]")
	}

	@Test
	fun depthBitArrayOf() {
		0.depthBitArrayOf(Bit.ZERO).string.assertEqualTo("[0]")
		0.depthBitArrayOf(Bit.ONE).string.assertEqualTo("[1]")
		1.depthBitArrayOf(Bit.ZERO).string.assertEqualTo("[00]")
		2.depthBitArrayOf(Bit.ONE).string.assertEqualTo("[1111]")
	}

	@Test
	fun setBit() {
		3.depthBitArray
			.set(Bit.ZERO, 2.depthBitArrayOf(Bit.ONE))
			.string
			.assertEqualTo("[11110000]")
		3.depthBitArray
			.set(Bit.ONE, 2.depthBitArrayOf(Bit.ONE))
			.string
			.assertEqualTo("[00001111]")
	}

	@Test
	fun setBitStream() {
		3.depthBitArray
			.set(stream(Bit.ZERO, Bit.ONE), 1.depthBitArrayOf(Bit.ONE))
			.string
			.assertEqualTo("[00110000]")
		3.depthBitArray
			.set(stream(Bit.ONE, Bit.ZERO), 1.depthBitArrayOf(Bit.ONE))
			.string
			.assertEqualTo("[00001100]")
	}

	@Test
	fun grow() {
		bitArray(Bit.ZERO, Bit.ONE)
			.grow
			.assertEqualTo(bitArray(Bit.ZERO, Bit.ONE, Bit.ZERO, Bit.ONE))
	}

	@Test
	fun inverse() {
		bitArray(Bit.ZERO, Bit.ONE)
			.inverse
			.assertEqualTo(bitArray(Bit.ONE, Bit.ZERO))
	}
}