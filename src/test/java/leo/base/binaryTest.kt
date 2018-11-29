package leo.base

import kotlin.test.Test

class BinaryTest {
	@Test
	fun binary() {
		binary(1, 0)
			.assertEqualTo(
				Binary(
					Bit.ONE,
					Binary(
						Bit.ZERO,
						null)))
	}

	@Test
	fun string() {
		binary(1, 0)
			.string
			.assertEqualTo("0b10")
	}

	@Test
	fun byteBinary() {
		18.toByte()
			.binary
			.assertEqualTo(
				binary(0, 0, 0, 1, 0, 0, 1, 0))
	}

	@Test
	fun shortBinary() {
		258.toShort()
			.binary
			.assertEqualTo(
				binary(
					0, 0, 0, 0, 0, 0, 0, 1,
					0, 0, 0, 0, 0, 0, 1, 0))
	}


	@Test
	fun intBinary() {
		65538
			.binary
			.assertEqualTo(
				binary(
					0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 1,
					0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 1, 0))
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
		2.binaryOrNullWithSize(1).assertEqualTo(binary(0))
		2.binaryOrNullWithSize(2).assertEqualTo(binary(1, 0))
		2.binaryOrNullWithSize(3).assertEqualTo(binary(0, 1, 0))
	}

//	@Test
//	fun align() {
//		binary(1, 1, 0, 1)
//			.align(2)
//			.assertEqualTo(binary(1, 1))
//		binary(1, 1, 0, 1)
//			.align(3)
//			.assertEqualTo(binary(1, 1, 0))
//		binary(1, 1, 0, 1)
//			.align(4)
//			.assertEqualTo(binary(1, 1, 0, 1))
//		binary(1, 1, 0, 1)
//			.align(5)
//			.assertEqualTo(binary(1, 1, 0, 1, 0))
//		binary(1, 1, 0, 1)
//			.align(6)
//			.assertEqualTo(binary(1, 1, 0, 1, 0, 0))
//	}
}