package leo.base

import kotlin.test.Test

class BinaryTest {
	@Test
	fun string() {
		stack(Bit.ONE, Bit.ZERO)
			.binary
			.string
			.assertEqualTo("0b10")
	}

	@Test
	fun byteBinary() {
		18.toByte()
			.binary
			.assertEqualTo(
				stack(
					Bit.ZERO,
					Bit.ZERO,
					Bit.ZERO,
					Bit.ONE,
					Bit.ZERO,
					Bit.ZERO,
					Bit.ONE,
					Bit.ZERO).binary)
	}

	@Test
	fun shortBinary() {
		258.toShort()
			.binary
			.assertEqualTo(
				stack(
					Bit.ZERO,
					Bit.ZERO,
					Bit.ZERO,
					Bit.ZERO,
					Bit.ZERO,
					Bit.ZERO,
					Bit.ZERO,
					Bit.ONE,

					Bit.ZERO,
					Bit.ZERO,
					Bit.ZERO,
					Bit.ZERO,
					Bit.ZERO,
					Bit.ZERO,
					Bit.ONE,
					Bit.ZERO).binary)
	}


	@Test
	fun intBinary() {
		65538
			.binary
			.assertEqualTo(
				stack(
					Bit.ZERO,
					Bit.ZERO,
					Bit.ZERO,
					Bit.ZERO,
					Bit.ZERO,
					Bit.ZERO,
					Bit.ZERO,
					Bit.ZERO,

					Bit.ZERO,
					Bit.ZERO,
					Bit.ZERO,
					Bit.ZERO,
					Bit.ZERO,
					Bit.ZERO,
					Bit.ZERO,
					Bit.ONE,

					Bit.ZERO,
					Bit.ZERO,
					Bit.ZERO,
					Bit.ZERO,
					Bit.ZERO,
					Bit.ZERO,
					Bit.ZERO,
					Bit.ZERO,

					Bit.ZERO,
					Bit.ZERO,
					Bit.ZERO,
					Bit.ZERO,
					Bit.ZERO,
					Bit.ZERO,
					Bit.ONE,
					Bit.ZERO).binary)
	}

	@Test
	fun clampedByte() {
		258.binary
			.clampedByte
			.assertEqualTo(2.toByte())
	}

	@Test
	fun binaryOrNullWithSize() {
		2.binaryOrNullWithSize(0).assertEqualTo(null)
		2.binaryOrNullWithSize(1).assertEqualTo(stack(Bit.ZERO).binary)
		2.binaryOrNullWithSize(2).assertEqualTo(stack(Bit.ONE, Bit.ZERO).binary)
		2.binaryOrNullWithSize(3).assertEqualTo(stack(Bit.ZERO, Bit.ONE, Bit.ZERO).binary)
	}
}