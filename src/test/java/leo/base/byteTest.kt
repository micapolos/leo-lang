package leo.base

import kotlin.test.Test

class ByteTest {
	@Test
	fun byteFromBits() {
		byte(
			Bit.ZERO,
			Bit.ZERO,
			Bit.ZERO,
			Bit.ZERO,
			Bit.ONE,
			Bit.ONE,
			Bit.ZERO,
			Bit.ONE)
			.assertEqualTo(13.byte)
	}

	@Test
	fun bitStream() {
		13.toByte()
			.bitStream
			.assertContains(
				Bit.ZERO,
				Bit.ZERO,
				Bit.ZERO,
				Bit.ZERO,
				Bit.ONE,
				Bit.ONE,
				Bit.ZERO,
				Bit.ONE)
	}

	@Test
	fun bitByteStream_incompleteByte() {
		stream(
			Bit.ZERO,
			Bit.ZERO,
			Bit.ZERO,
			Bit.ZERO,
			Bit.ONE,
			Bit.ONE,
			Bit.ZERO)
			.bitByteStreamOrNull
			.assertEqualTo(null)
	}

	@Test
	fun bitByteStream_oneByte() {
		stream(
			Bit.ZERO,
			Bit.ZERO,
			Bit.ZERO,
			Bit.ZERO,
			Bit.ONE,
			Bit.ONE,
			Bit.ZERO,
			Bit.ONE)
			.bitByteStreamOrNull
			?.stack
			.assertEqualTo(stack(13.byte))
	}

	@Test
	fun bitByteStream_twoBytes() {
		stream(
			Bit.ZERO,
			Bit.ZERO,
			Bit.ZERO,
			Bit.ZERO,
			Bit.ONE,
			Bit.ONE,
			Bit.ZERO,
			Bit.ONE,
			Bit.ZERO,
			Bit.ZERO,
			Bit.ZERO,
			Bit.ZERO,
			Bit.ONE,
			Bit.ONE,
			Bit.ONE,
			Bit.ZERO)
			.bitByteStreamOrNull
			?.stack
			.assertEqualTo(stack(13.byte, 14.byte))
	}

	@Test
	fun byteBitStream() {
		stream(13.byte, 14.byte)
			.byteBitStream
			.stack
			.assertEqualTo(
				stack(
					Bit.ZERO,
					Bit.ZERO,
					Bit.ZERO,
					Bit.ZERO,
					Bit.ONE,
					Bit.ONE,
					Bit.ZERO,
					Bit.ONE,
					Bit.ZERO,
					Bit.ZERO,
					Bit.ZERO,
					Bit.ZERO,
					Bit.ONE,
					Bit.ONE,
					Bit.ONE,
					Bit.ZERO))
	}
}
