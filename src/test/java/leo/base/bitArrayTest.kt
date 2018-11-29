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
		0.depthBitArrayOf(0.bit).assertEqualTo(bitArray(0.bit))
		0.depthBitArrayOf(1.bit).assertEqualTo(bitArray(1.bit))
		1.depthBitArrayOf(0.bit).assertEqualTo(bitArray(0.bit, 0.bit))
		2.depthBitArrayOf(1.bit).assertEqualTo(bitArray(1.bit, 1.bit, 1.bit, 1.bit))
	}

	@Test
	fun setBit() {
		bitArray(0.bit, 0.bit, 0.bit, 0.bit)
			.set(0.bit, bitArray(1.bit, 1.bit))
			.assertEqualTo(bitArray(1.bit, 1.bit, 0.bit, 0.bit))
		bitArray(0.bit, 0.bit, 0.bit, 0.bit)
			.set(1.bit, 1.depthBitArrayOf(1.bit))
			.assertEqualTo(bitArray(0.bit, 0.bit, 1.bit, 1.bit))
	}

	@Test
	fun set() {
		bitArray(0.bit, 0.bit)
			.set(bitArray(1.bit, 1.bit))
			.assertEqualTo(bitArray(1.bit, 1.bit))
		bitArray(0.bit, 0.bit)
			.set(bitArray(1.bit))
			.assertEqualTo(null)
	}

	@Test
	fun setBitStream() {
		bitArray(0.bit, 0.bit, 0.bit, 0.bit)
			.set(nullOf<Stream<Bit>>(), bitArray(1.bit, 1.bit, 1.bit, 1.bit))
			.assertEqualTo(bitArray(1.bit, 1.bit, 1.bit, 1.bit))
		bitArray(0.bit, 0.bit, 0.bit, 0.bit)
			.set(stream(0.bit), bitArray(1.bit, 1.bit))
			.assertEqualTo(bitArray(1.bit, 1.bit, 0.bit, 0.bit))
		bitArray(0.bit, 0.bit, 0.bit, 0.bit)
			.set(stream(1.bit, 0.bit), bitArray(1.bit))
			.assertEqualTo(bitArray(0.bit, 0.bit, 1.bit, 0.bit))
		bitArray(0.bit, 0.bit, 0.bit, 0.bit)
			.set(stream(1.bit, 0.bit, 1.bit), 0.depthBitArrayOf(1.bit))
			.assertEqualTo(null)
	}

	@Test
	fun incrementDepth() {
		bitArray(0.bit, 1.bit)
			.incrementDepth
			.assertEqualTo(bitArray(0.bit, 1.bit, 0.bit, 1.bit))
	}

	@Test
	fun increaseDepth() {
		bitArray(0.bit, 1.bit)
			.increaseDepth(3)
			.assertEqualTo(bitArray(0.bit, 1.bit, 0.bit, 1.bit, 0.bit, 1.bit, 0.bit, 1.bit))
	}

	@Test
	fun inverse() {
		bitArray(0.bit, 1.bit)
			.inverse
			.assertEqualTo(bitArray(1.bit, 0.bit))
	}

	@Test
	fun getSetPrimitives() {
		val array = 5.depthBitArray
			.set(binary(0.bit, 0.bit), 0x01.toByte().bitArray)
			?.set(binary(0.bit, 1.bit), 0x02.toByte().bitArray)
			?.set(binary(1.bit, 0.bit), 0x04.toByte().bitArray)
			?.set(binary(1.bit, 1.bit), 0x08.toByte().bitArray)

		array.assertNotNull

		array?.get(binary(0.bit, 0.bit))?.byteOrNull.assertEqualTo(0x01.toByte())
		array?.get(binary(0.bit, 1.bit))?.byteOrNull.assertEqualTo(0x02.toByte())
		array?.get(binary(1.bit, 0.bit))?.byteOrNull.assertEqualTo(0x04.toByte())
		array?.get(binary(1.bit, 1.bit))?.byteOrNull.assertEqualTo(0x08.toByte())

		array?.get(binary(0.bit))?.shortOrNull.assertEqualTo(0x0102.toShort())
		array?.get(binary(1.bit))?.shortOrNull.assertEqualTo(0x0408.toShort())

		array?.intOrNull.assertEqualTo(0x01020408)
	}
}