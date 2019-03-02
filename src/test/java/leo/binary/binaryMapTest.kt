package leo.binary

import leo.base.Bit
import leo.base.assertEqualTo
import org.junit.Test

class BinaryMapTest {
	@Test
	fun emptyBinaryMap() {
		emptyBinaryMap<Int>().assertEqualTo(BinaryMap(emptyBitMap()))
	}

	@Test
	fun bitBinaryMap() {
		binaryMap(Bit.ZERO, 0).assertEqualTo(BinaryMap(bitMap(Bit.ZERO, BinaryMapFullMatch(0))))
		binaryMap(Bit.ONE, 1).assertEqualTo(BinaryMap(bitMap(Bit.ONE, BinaryMapFullMatch(1))))
	}

	@Test
	fun binaryBinaryMap() {
		binaryMap(binary(Bit.ZERO, Bit.ZERO, Bit.ONE, Bit.ONE), 0)
			.assertEqualTo(BinaryMap(bitMap(Bit.ZERO, BinaryMapPartialMatch(binaryMap(binary(Bit.ZERO, Bit.ONE, Bit.ONE), 0)))))
	}

	@Test
	fun get() {
		val binaryMap =
			BinaryMap(BitMap(
				BinaryMapFullMatch(0),
				BinaryMapPartialMatch(BinaryMap(BitMap(
					BinaryMapFullMatch(1),
					null)))))
		binaryMap.get(binary(Bit.ZERO)).assertEqualTo(0)
		binaryMap.get(binary(Bit.ONE)).assertEqualTo(null)
		binaryMap.get(binary(Bit.ZERO, Bit.ZERO)).assertEqualTo(null)
		binaryMap.get(binary(Bit.ZERO, Bit.ONE)).assertEqualTo(null)
		binaryMap.get(binary(Bit.ONE, Bit.ZERO)).assertEqualTo(1)
		binaryMap.get(binary(Bit.ONE, Bit.ONE)).assertEqualTo(null)
	}

	@Test
	fun plus() {
		BinaryMap<Int>(BitMap(null, null))
			.plus(binary(Bit.ZERO), 0)
			.assertEqualTo(BinaryMap(BitMap(BinaryMapFullMatch(0), null)))

		BinaryMap<Int>(BitMap(null, null))
			.plus(binary(Bit.ONE), 0)
			.assertEqualTo(BinaryMap(BitMap(null, BinaryMapFullMatch(0))))

		BinaryMap(BitMap(BinaryMapFullMatch(0), null))
			.plus(binary(Bit.ZERO), 1)
			.assertEqualTo(null)

		BinaryMap(BitMap(BinaryMapFullMatch(0), null))
			.plus(binary(Bit.ONE), 1)
			.assertEqualTo(BinaryMap(BitMap(BinaryMapFullMatch(0), BinaryMapFullMatch(1))))

		BinaryMap(BitMap(BinaryMapPartialMatch(BinaryMap(BitMap(BinaryMapFullMatch(0), null))), null))
			.plus(binary(Bit.ZERO), 1)
			.assertEqualTo(null)

		BinaryMap(BitMap(BinaryMapPartialMatch(BinaryMap(BitMap(BinaryMapFullMatch(0), null))), null))
			.plus(binary(Bit.ZERO, Bit.ZERO), 1)
			.assertEqualTo(null)

		BinaryMap(BitMap(BinaryMapPartialMatch(BinaryMap(BitMap(BinaryMapFullMatch(0), null))), null))
			.plus(binary(Bit.ZERO, Bit.ONE), 1)
			.assertEqualTo(
				BinaryMap(BitMap(
					BinaryMapPartialMatch(BinaryMap(BitMap(BinaryMapFullMatch(0), BinaryMapFullMatch(1)))),
					null)))

		BinaryMap(BitMap(BinaryMapPartialMatch(BinaryMap(BitMap(BinaryMapFullMatch(0), null))), null))
			.plus(binary(Bit.ONE), 1)
			.assertEqualTo(
				BinaryMap(BitMap(
					BinaryMapPartialMatch(BinaryMap(BitMap(BinaryMapFullMatch(0), null))),
					BinaryMapFullMatch(1))))

		BinaryMap(BitMap(BinaryMapPartialMatch(BinaryMap(BitMap(BinaryMapFullMatch(0), null))), null))
			.plus(binary(Bit.ONE, Bit.ONE), 1)
			.assertEqualTo(
				BinaryMap(BitMap(
					BinaryMapPartialMatch(BinaryMap(BitMap(BinaryMapFullMatch(0), null))),
					BinaryMapPartialMatch(BinaryMap(BitMap(null, BinaryMapFullMatch(1)))))))
	}
}