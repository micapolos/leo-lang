package leo.base

import kotlin.test.Test

class IntTest {
	@Test
	fun indexSize() {
		0.indexSize.assertEqualTo(32) // size 0 is like MAX_INT
		1.indexSize.assertEqualTo(0)
		2.indexSize.assertEqualTo(1)
		3.indexSize.assertEqualTo(2)
		4.indexSize.assertEqualTo(2)
		256.indexSize.assertEqualTo(8)
		257.indexSize.assertEqualTo(9)
		65536.indexSize.assertEqualTo(16)
		65537.indexSize.assertEqualTo(17)
	}

	@Test
	fun bitMaskOrNull() {
		0.bitMaskOrNull.assertEqualTo(0x00)
		1.bitMaskOrNull.assertEqualTo(0x01)
		8.bitMaskOrNull.assertEqualTo(0xFF)
		31.bitMaskOrNull.assertEqualTo(0x7FFFFFFF)
		32.bitMaskOrNull.assertEqualTo(-1)
		(-1).bitMaskOrNull.assertEqualTo(null)
		33.bitMaskOrNull.assertEqualTo(null)
	}

	@Test
	fun pow2() {
		(-1).pow2.assertEqualTo(0)
		0.pow2.assertEqualTo(1)
		1.pow2.assertEqualTo(2)
		2.pow2.assertEqualTo(4)
		30.pow2.assertEqualTo(0x40000000)
		31.pow2.assertEqualTo(Integer.MIN_VALUE)
		32.pow2.assertEqualTo(0)
	}

	@Test
	fun bits() {
		13.bitSeq(0).map(EnumBit::int).assertContains()
		13.bitSeq(4).map(EnumBit::int).assertContains(1, 1, 0, 1)
		13.bitSeq(64).map(EnumBit::int).assertContains(
			0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 1, 1, 0, 1)
	}

	@Test
	fun bytes() {
		0x12345678.byte(3).assertEqualTo(0x12.clampedByte)
		0x12345678.byte(2).assertEqualTo(0x34.clampedByte)
		0x12345678.byte(1).assertEqualTo(0x56.clampedByte)
		0x12345678.byte(0).assertEqualTo(0x78.clampedByte)
		0x12345678.setByte(3, 0x01).assertEqualTo(0x01345678)
		0x12345678.setByte(2, 0x01).assertEqualTo(0x12015678)
		0x12345678.setByte(1, 0x01).assertEqualTo(0x12340178)
		0x12345678.setByte(0, 0x01).assertEqualTo(0x12345601)
	}

	@Test
	fun nearestPot() {
		0.nearestPot.assertEqualTo(0)
		1.nearestPot.assertEqualTo(1)
		2.nearestPot.assertEqualTo(2)
		3.nearestPot.assertEqualTo(4)
		4.nearestPot.assertEqualTo(4)
		7.nearestPot.assertEqualTo(8)
		8.nearestPot.assertEqualTo(8)
		15.nearestPot.assertEqualTo(16)
		16.nearestPot.assertEqualTo(16)
	}
}