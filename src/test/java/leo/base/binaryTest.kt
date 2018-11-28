package leo.base

import kotlin.test.Test

class BinaryTest {
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
		65538.toShort()
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
}