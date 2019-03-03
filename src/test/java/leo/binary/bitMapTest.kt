package leo.binary

import leo.base.assertEqualTo
import org.junit.Test

class ChoiceTest {
	@Test
	fun empty() {
		emptyChoice<Int>().assertEqualTo(Choice(null, null))
	}

	@Test
	fun withBit() {
		array(Bit.ZERO, 0).assertEqualTo(Choice(0, null))
		array(Bit.ONE, 1).assertEqualTo(Choice(null, 1))
	}

	@Test
	fun get() {
		Choice(0, 1).get(Bit.ZERO).assertEqualTo(0)
		Choice(0, 1).get(Bit.ONE).assertEqualTo(1)
	}

	@Test
	fun set() {
		Choice(0, 1).set(Bit.ZERO, 2).assertEqualTo(Choice(2, 1))
		Choice(0, 1).set(Bit.ONE, 2).assertEqualTo(Choice(0, 2))
	}

	@Test
	fun plus() {
		Choice(0, null).plus(Bit.ZERO, 2).assertEqualTo(null)
		Choice(0, null).plus(Bit.ONE, 2).assertEqualTo(Choice(0, 2))
		Choice(null, 1).plus(Bit.ZERO, 2).assertEqualTo(Choice(2, 1))
		Choice(null, 1).plus(Bit.ONE, 2).assertEqualTo(null)
	}
}