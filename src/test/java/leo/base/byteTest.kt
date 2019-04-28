package leo.base

import leo.binary.bitByteSeq
import leo.binary.byteBitSeq
import leo.binary.utf8ByteSeq
import leo.binary.utf8String
import kotlin.test.Test

class ByteTest {
	@Test
	fun byteFromBits() {
		byte(
			EnumBit.ZERO,
			EnumBit.ZERO,
			EnumBit.ZERO,
			EnumBit.ZERO,
			EnumBit.ONE,
			EnumBit.ONE,
			EnumBit.ZERO,
			EnumBit.ONE)
			.assertEqualTo(13.clampedByte)
	}

	@Test
	fun bitStream() {
		13.toByte()
			.bitStream
			.assertContains(
				EnumBit.ZERO,
				EnumBit.ZERO,
				EnumBit.ZERO,
				EnumBit.ZERO,
				EnumBit.ONE,
				EnumBit.ONE,
				EnumBit.ZERO,
				EnumBit.ONE)
	}

	@Test
	fun bitByteStream_incompleteByte() {
		stream(
			EnumBit.ZERO,
			EnumBit.ZERO,
			EnumBit.ZERO,
			EnumBit.ZERO,
			EnumBit.ONE,
			EnumBit.ONE,
			EnumBit.ZERO)
			.bitByteStreamOrNull
			.assertEqualTo(null)
	}

	@Test
	fun bitByteStream_oneByte() {
		stream(
			EnumBit.ZERO,
			EnumBit.ZERO,
			EnumBit.ZERO,
			EnumBit.ZERO,
			EnumBit.ONE,
			EnumBit.ONE,
			EnumBit.ZERO,
			EnumBit.ONE)
			.bitByteStreamOrNull
			?.stack
			.assertEqualTo(stack(13.clampedByte))
	}

	@Test
	fun bitByteStream_twoBytes() {
		stream(
			EnumBit.ZERO,
			EnumBit.ZERO,
			EnumBit.ZERO,
			EnumBit.ZERO,
			EnumBit.ONE,
			EnumBit.ONE,
			EnumBit.ZERO,
			EnumBit.ONE,
			EnumBit.ZERO,
			EnumBit.ZERO,
			EnumBit.ZERO,
			EnumBit.ZERO,
			EnumBit.ONE,
			EnumBit.ONE,
			EnumBit.ONE,
			EnumBit.ZERO)
			.bitByteStreamOrNull
			?.stack
			.assertEqualTo(stack(13.clampedByte, 14.clampedByte))
	}

	@Test
	fun byteBitStream() {
		stream(13.clampedByte, 14.clampedByte)
			.byteBitStream
			.stack
			.assertEqualTo(
				stack(
					EnumBit.ZERO,
					EnumBit.ZERO,
					EnumBit.ZERO,
					EnumBit.ZERO,
					EnumBit.ONE,
					EnumBit.ONE,
					EnumBit.ZERO,
					EnumBit.ONE,
					EnumBit.ZERO,
					EnumBit.ZERO,
					EnumBit.ZERO,
					EnumBit.ZERO,
					EnumBit.ONE,
					EnumBit.ONE,
					EnumBit.ONE,
					EnumBit.ZERO))
	}

	@Test
	fun byteToBitStreamOrNull() {
		stream(
			EnumBit.ZERO,
			EnumBit.ZERO,
			EnumBit.ZERO,
			EnumBit.ZERO,
			EnumBit.ONE,
			EnumBit.ONE,
			EnumBit.ZERO,
			EnumBit.ONE,
			EnumBit.ONE)
			.bitParseByte
			.assertParsedAndRest(13.toByte(), EnumBit.ONE.onlyStream)
	}

	@Test
	fun intByteConversion() {
		0x01020304
			.setByte3(0x10.toByte())
			.setByte2(0x20.toByte())
			.setByte1(0x30.toByte())
			.setByte0(0x40.toByte())
			.assertEqualTo(0x10203040)
	}

	@Test
	fun bitByteSeq() {
		"fąfel".utf8ByteSeq.byteBitSeq.bitByteSeq.utf8String.assertEqualTo("fąfel")
	}
}

