package leo.binary

import leo.base.assertEqualTo
import org.junit.Test

class BinaryMapTest {
	@Test
	fun emptyBinaryMap() {
		emptyBinaryMap<Int>().assertEqualTo(BinaryMap(emptyBitMap()))
	}

	@Test
	fun bitBinaryMap() {
		binaryMap(Bit0, 0).assertEqualTo(BinaryMap(bitMap(Bit0, BinaryMapFullMatch(0))))
		binaryMap(Bit1, 1).assertEqualTo(BinaryMap(bitMap(Bit1, BinaryMapFullMatch(1))))
	}

	@Test
	fun binaryBinaryMap() {
		binaryMap(binary(Bit0, Bit0, Bit1, Bit1), 0)
			.assertEqualTo(BinaryMap(bitMap(Bit0, BinaryMapPartialMatch(binaryMap(binary(Bit0, Bit1, Bit1), 0)))))
	}

	@Test
	fun plus() {
		BinaryMap<Int>(BitMap(null, null))
			.plus(binary(Bit0), 0)
			.assertEqualTo(BinaryMap(BitMap(BinaryMapFullMatch(0), null)))

		BinaryMap<Int>(BitMap(null, null))
			.plus(binary(Bit1), 0)
			.assertEqualTo(BinaryMap(BitMap(null, BinaryMapFullMatch(0))))

		BinaryMap(BitMap(BinaryMapFullMatch(0), null))
			.plus(binary(Bit0), 1)
			.assertEqualTo(null)

		BinaryMap(BitMap(BinaryMapFullMatch(0), null))
			.plus(binary(Bit1), 1)
			.assertEqualTo(BinaryMap(BitMap(BinaryMapFullMatch(0), BinaryMapFullMatch(1))))

		BinaryMap(BitMap(BinaryMapPartialMatch(BinaryMap(BitMap(BinaryMapFullMatch(0), null))), null))
			.plus(binary(Bit0), 1)
			.assertEqualTo(null)

		BinaryMap(BitMap(BinaryMapPartialMatch(BinaryMap(BitMap(BinaryMapFullMatch(0), null))), null))
			.plus(binary(Bit0, Bit0), 1)
			.assertEqualTo(null)

		BinaryMap(BitMap(BinaryMapPartialMatch(BinaryMap(BitMap(BinaryMapFullMatch(0), null))), null))
			.plus(binary(Bit0, Bit1), 1)
			.assertEqualTo(
				BinaryMap(BitMap(
					BinaryMapPartialMatch(BinaryMap(BitMap(BinaryMapFullMatch(0), BinaryMapFullMatch(1)))),
					null)))

		BinaryMap(BitMap(BinaryMapPartialMatch(BinaryMap(BitMap(BinaryMapFullMatch(0), null))), null))
			.plus(binary(Bit1), 1)
			.assertEqualTo(
				BinaryMap(BitMap(
					BinaryMapPartialMatch(BinaryMap(BitMap(BinaryMapFullMatch(0), null))),
					BinaryMapFullMatch(1))))

		BinaryMap(BitMap(BinaryMapPartialMatch(BinaryMap(BitMap(BinaryMapFullMatch(0), null))), null))
			.plus(binary(Bit1, Bit1), 1)
			.assertEqualTo(
				BinaryMap(BitMap(
					BinaryMapPartialMatch(BinaryMap(BitMap(BinaryMapFullMatch(0), null))),
					BinaryMapPartialMatch(BinaryMap(BitMap(null, BinaryMapFullMatch(1)))))))
	}
}