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
		0.depthBitArray(0.bit).assertEqualTo(bitArray(0))
		0.depthBitArray(1.bit).assertEqualTo(bitArray(1))
		1.depthBitArray(0.bit).assertEqualTo(bitArray(0, 0))
		2.depthBitArray(1.bit).assertEqualTo(bitArray(1, 1, 1, 1))
	}

	@Test
	fun setBit() {
		bitArray(0, 0, 0, 0)
			.set(0.bit, bitArray(1, 1))
			.assertEqualTo(bitArray(1, 1, 0, 0))
		bitArray(0, 0, 0, 0)
			.set(1.bit, 1.depthBitArray(1.bit))
			.assertEqualTo(bitArray(0, 0, 1, 1))
	}

	@Test
	fun set() {
		bitArray(0, 0)
			.set(bitArray(1, 1))
			.assertEqualTo(bitArray(1, 1))
		bitArray(0, 0)
			.set(bitArray(1))
			.assertEqualTo(null)
	}

	@Test
	fun setBinary() {
		bitArray(0, 0, 0, 0)
			.set(nullOf<Binary>(), bitArray(1, 1, 1, 1))
			.assertEqualTo(bitArray(1, 1, 1, 1))
		bitArray(0, 0, 0, 0)
			.set(binary(0), bitArray(1, 1))
			.assertEqualTo(bitArray(1, 1, 0, 0))
		bitArray(0, 0, 0, 0)
			.set(binary(1, 0), bitArray(1))
			.assertEqualTo(bitArray(0, 0, 1, 0))
		bitArray(0, 0, 0, 0)
			.set(binary(1, 0, 1), bitArray(1))
			.assertEqualTo(null)
	}

	@Test
	fun incrementDepth() {
		bitArray(0, 1)
			.incrementDepth
			.assertEqualTo(bitArray(0, 1, 0, 1))
	}

	@Test
	fun increaseDepth() {
		bitArray(0, 1)
			.increaseDepth(3)
			.assertEqualTo(bitArray(0, 1, 0, 1, 0, 1, 0, 1))
	}

	@Test
	fun inverse() {
		bitArray(0, 1)
			.inverse
			.assertEqualTo(bitArray(1, 0))
	}

	@Test
	fun getSetPrimitives() {
		val array = 5.depthBitArray
			.set(binary(0, 0), 0x01.toByte().bitArray)
			?.set(binary(0, 1), 0x02.toByte().bitArray)
			?.set(binary(1, 0), 0x04.toByte().bitArray)
			?.set(binary(1, 1), 0x08.toByte().bitArray)

		array.assertNotNull

		array?.get(binary(0, 0))?.byteOrNull.assertEqualTo(0x01.toByte())
		array?.get(binary(0, 1))?.byteOrNull.assertEqualTo(0x02.toByte())
		array?.get(binary(1, 0))?.byteOrNull.assertEqualTo(0x04.toByte())
		array?.get(binary(1, 1))?.byteOrNull.assertEqualTo(0x08.toByte())

		array?.get(binary(0))?.shortOrNull.assertEqualTo(0x0102.toShort())
		array?.get(binary(1))?.shortOrNull.assertEqualTo(0x0408.toShort())

		array?.intOrNull.assertEqualTo(0x01020408)
	}

	@Test
	fun minIndexBinaryOrNull() {
		bitArray(0).minIndexBinaryOrNull.assertEqualTo(null)
		bitArray(0, 1).minIndexBinaryOrNull.assertEqualTo(binary(0))
		bitArray(1, 0, 1, 1).minIndexBinaryOrNull.assertEqualTo(binary(0, 0))
	}

	@Test
	fun maxIndexBinaryOrNull() {
		bitArray(0).maxIndexBinaryOrNull.assertEqualTo(null)
		bitArray(0, 1).maxIndexBinaryOrNull.assertEqualTo(binary(1))
		bitArray(1, 0, 1, 1).maxIndexBinaryOrNull.assertEqualTo(binary(1, 1))
	}

	@Test
	fun indexedBitStream() {
		bitArray(1, 0, 1, 1)
			.indexedBitStream
			.assertContains(
				binary(0, 0).indexedTo(1.bit),
				binary(0, 1).indexedTo(0.bit),
				binary(1, 0).indexedTo(1.bit),
				binary(1, 1).indexedTo(1.bit))
	}
}