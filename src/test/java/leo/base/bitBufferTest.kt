package leo.base

import leo.binary.Bit
import kotlin.test.Test

class BitBufferTest {
	@Test
	fun singletonBitArrayBitBuffer() {
		bitArray(0)
			.bitBuffer
			.assertEqualTo(BitBuffer(bitArray(0), null, null))
	}

	@Test
	fun compositeBitArrayBitBuffer() {
		bitArray(1, 0, 1, 1)
			.bitBuffer
			.assertEqualTo(BitBuffer(bitArray(1, 0, 1, 1), binary(0, 0), binary(1, 1)))
	}

	@Test
	fun singletonString() {
		bitArray(0)
			.bitBuffer
			.string
			.assertEqualTo("[<0>]")
	}

	@Test
	fun fullString() {
		bitArray(0, 1)
			.bitBuffer
			.string
			.assertEqualTo("[<01>]")
	}

	@Test
	fun partialString() {
		bitArray(0, 1, 1, 1, 0, 1, 0, 1)
			.bitBuffer
			.tailPop
			.tailPop
			.headPop
			.string
			.assertEqualTo("[01<11010>1]")
	}

	@Test
	fun pop() {
		val bitBuffer = bitArray(0, 1).bitBuffer

		bitBuffer.tailBit.assertEqualTo(Bit.ZERO)
		bitBuffer.tailPop.tailBit.assertEqualTo(Bit.ONE)
		bitBuffer.tailPop.tailPop.tailBit.assertEqualTo(Bit.ZERO)

		bitBuffer.headBit.assertEqualTo(Bit.ONE)
		bitBuffer.headPop.headBit.assertEqualTo(Bit.ZERO)
		bitBuffer.headPop.headPop.headBit.assertEqualTo(Bit.ONE)
	}

	@Test
	fun headPushAndSet() {
		bitArray(0, 0, 0, 0).bitBuffer
			.headPush
			.headSet(Bit.ONE)
			.headPush
			.headPush
			.headSet(Bit.ONE)
			.headPush
			.headSet(Bit.ONE)
			.assertEqualTo(bitArray(1, 0, 1, 1).bitBuffer)
	}

	@Test
	fun tailPushAndSet() {
		bitArray(0, 0, 0, 0).bitBuffer
			.tailPush
			.tailSet(Bit.ONE)
			.tailPush
			.tailPush
			.tailSet(Bit.ONE)
			.tailPush
			.tailSet(Bit.ONE)
			.assertEqualTo(bitArray(1, 1, 0, 1).bitBuffer)
	}

	@Test
	fun performance() {
		16.depthBitArray(Bit.ZERO)
			.bitBuffer
			.iterate(65536) {
				headPush.headSet(Bit.ONE)
			}
			.assertEqualTo(16.depthBitArray(Bit.ONE).bitBuffer)
	}
}