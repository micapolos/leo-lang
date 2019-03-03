package leo.base

import leo.binary.bit
import kotlin.test.Test

class NaturalTest {
	@Test
	fun string() {
		Natural(null).string.assertEqualTo("0b1")
		Natural(stack(1.bit, 0.bit, 1.bit)).string.assertEqualTo("0b1101")
	}

	@Test
	fun plusOne() {
		nullOf<Natural>().orNullPlusOne.assertEqualTo(Natural(null))
		Natural(null).plusOne.assertEqualTo(Natural(stack(0.bit)))
		Natural(stack(0.bit)).plusOne.assertEqualTo(Natural(stack(1.bit)))
		Natural(stack(1.bit)).plusOne.assertEqualTo(Natural(stack(0.bit, 0.bit)))
		Natural(stack(1.bit, 0.bit)).plusOne.assertEqualTo(Natural(stack(1.bit, 1.bit)))
		Natural(stack(1.bit, 1.bit)).plusOne.assertEqualTo(Natural(stack(0.bit, 0.bit, 0.bit)))
	}

	@Test
	fun minusOne() {
		Natural(null).minusOne.assertEqualTo(null)
		Natural(stack(0.bit)).minusOne.assertEqualTo(Natural(null))
		Natural(stack(1.bit)).minusOne.assertEqualTo(Natural(stack(0.bit)))
		Natural(stack(1.bit, 0.bit)).minusOne.assertEqualTo(Natural(stack(0.bit, 1.bit)))
		Natural(stack(1.bit, 1.bit)).minusOne.assertEqualTo(Natural(stack(1.bit, 0.bit)))
		Natural(stack(0.bit, 0.bit, 0.bit)).minusOne.assertEqualTo(Natural(stack(1.bit, 1.bit)))
	}

	@Test
	fun bitCount() {
		naturalOne.bitCount.assertEqualTo(naturalOne)
		naturalTwo.bitCount.assertEqualTo(naturalTwo)
		naturalThree.bitCount.assertEqualTo(naturalTwo)
		naturalFour.bitCount.assertEqualTo(naturalThree)
		naturalFive.bitCount.assertEqualTo(naturalThree)
		naturalSix.bitCount.assertEqualTo(naturalThree)
		naturalSeven.bitCount.assertEqualTo(naturalThree)
	}

	@Test
	fun intNatural() {
		1.unsignedNaturalOrNull!!.assertEqualTo(naturalOne)
		2.unsignedNaturalOrNull!!.assertEqualTo(naturalTwo)
		3.unsignedNaturalOrNull!!.assertEqualTo(naturalThree)
		4.unsignedNaturalOrNull!!.assertEqualTo(naturalFour)
		5.unsignedNaturalOrNull!!.assertEqualTo(naturalFive)
		6.unsignedNaturalOrNull!!.assertEqualTo(naturalSix)
		7.unsignedNaturalOrNull!!.assertEqualTo(naturalSeven)
	}

	@Test
	fun speedTest() {
		naturalOne
			.iterate(1.shl(24).dec(), Natural::plusOne)
			.assertEqualTo(1.shl(24).unsignedNaturalOrNull!!)
	}
}