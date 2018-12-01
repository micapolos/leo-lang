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
	fun bitCount() {
		binary(0).bitCountInt.assertEqualTo(1)
		binary(0, 0).bitCountInt.assertEqualTo(2)
		binary(0, 0, 0).bitCountInt.assertEqualTo(3)
		binary(0, 0, 0, 0).bitCountInt.assertEqualTo(4)
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

	@Test
	fun carryIncrement() {
		binary(0).carryIncrement.assertEqualTo(Bit.ZERO to binary(1))
		binary(1).carryIncrement.assertEqualTo(Bit.ONE to binary(0))
		binary(0, 0).carryIncrement.assertEqualTo(Bit.ZERO to binary(0, 1))
		binary(0, 1).carryIncrement.assertEqualTo(Bit.ZERO to binary(1, 0))
		binary(1, 0).carryIncrement.assertEqualTo(Bit.ZERO to binary(1, 1))
		binary(1, 1).carryIncrement.assertEqualTo(Bit.ONE to binary(0, 0))
	}

	@Test
	fun increment() {
		binary(0).increment.assertEqualTo(binary(1))
		binary(1).increment.assertEqualTo(null)
		binary(0, 0).increment.assertEqualTo(binary(0, 1))
		binary(0, 1).increment.assertEqualTo(binary(1, 0))
		binary(1, 0).increment.assertEqualTo(binary(1, 1))
		binary(1, 1).increment.assertEqualTo(null)
	}

	@Test
	fun incrementAndGrow() {
		binary(0).incrementAndGrow.assertEqualTo(binary(1))
		binary(1).incrementAndGrow.assertEqualTo(binary(1, 0))
		binary(0, 0).incrementAndGrow.assertEqualTo(binary(0, 1))
		binary(0, 1).incrementAndGrow.assertEqualTo(binary(1, 0))
		binary(1, 0).incrementAndGrow.assertEqualTo(binary(1, 1))
		binary(1, 1).incrementAndGrow.assertEqualTo(binary(1, 0, 0))
	}

	@Test
	fun incrementAndClamp() {
		binary(0).incrementAndClamp.assertEqualTo(binary(1))
		binary(1).incrementAndClamp.assertEqualTo(binary(1))
		binary(0, 0).incrementAndClamp.assertEqualTo(binary(0, 1))
		binary(0, 1).incrementAndClamp.assertEqualTo(binary(1, 0))
		binary(1, 0).incrementAndClamp.assertEqualTo(binary(1, 1))
		binary(1, 1).incrementAndClamp.assertEqualTo(binary(1, 1))
	}

	@Test
	fun incrementAndWrap() {
		binary(0).incrementAndWrap.assertEqualTo(binary(1))
		binary(1).incrementAndWrap.assertEqualTo(binary(0))
		binary(0, 0).incrementAndWrap.assertEqualTo(binary(0, 1))
		binary(0, 1).incrementAndWrap.assertEqualTo(binary(1, 0))
		binary(1, 0).incrementAndWrap.assertEqualTo(binary(1, 1))
		binary(1, 1).incrementAndWrap.assertEqualTo(binary(0, 0))
	}

	@Test
	fun borrowDecrement() {
		binary(0).borrowDecrement.assertEqualTo(Bit.ONE to binary(1))
		binary(1).borrowDecrement.assertEqualTo(Bit.ZERO to binary(0))
		binary(0, 0).borrowDecrement.assertEqualTo(Bit.ONE to binary(1, 1))
		binary(0, 1).borrowDecrement.assertEqualTo(Bit.ZERO to binary(0, 0))
		binary(1, 0).borrowDecrement.assertEqualTo(Bit.ZERO to binary(0, 1))
		binary(1, 1).borrowDecrement.assertEqualTo(Bit.ZERO to binary(1, 0))
	}

	@Test
	fun decrement() {
		binary(0).decrement.assertEqualTo(null)
		binary(1).decrement.assertEqualTo(binary(0))
		binary(0, 0).decrement.assertEqualTo(null)
		binary(0, 1).decrement.assertEqualTo(binary(0, 0))
		binary(1, 0).decrement.assertEqualTo(binary(0, 1))
		binary(1, 1).decrement.assertEqualTo(binary(1, 0))
	}

	@Test
	fun wrapIndexedStream_nullIndex() {
		stream(0, 1)
			.wrapIndexedStream(null)
			.assertContains(
				nullOf<Binary>().indexedTo(0),
				nullOf<Binary>().indexedTo(1))
	}

	@Test
	fun wrapIndexedStream_nonNullIndex() {
		stream(0, 1, 2, 3, 4, 5)
			.wrapIndexedStream(binary(0, 1))
			.assertContains(
				binary(0, 1).indexedTo(0),
				binary(1, 0).indexedTo(1),
				binary(1, 1).indexedTo(2),
				binary(0, 0).indexedTo(3),
				binary(0, 1).indexedTo(4),
				binary(1, 0).indexedTo(5))
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