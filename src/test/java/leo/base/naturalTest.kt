package leo.base

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
	fun intNatural() {
		13.unsignedNaturalOrNull!!.assertEqualTo(Natural(stack(1.bit, 0.bit, 1.bit)))
	}

	@Test
	fun speedTest() {
		naturalOne
			.iterate(1.shl(24).dec(), Natural::plusOne)
			.assertEqualTo(1.shl(24).unsignedNaturalOrNull!!)
	}
}