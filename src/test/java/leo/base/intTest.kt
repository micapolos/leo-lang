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
		13.bitSequence(0).map(EnumBit::int).assertContains()
		13.bitSequence(4).map(EnumBit::int).assertContains(1, 1, 0, 1)
		13.bitSequence(64).map(EnumBit::int).assertContains(
			0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 1, 1, 0, 1)
	}
}