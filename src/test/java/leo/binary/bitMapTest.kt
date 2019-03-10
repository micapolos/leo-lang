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
		array(zero.bit, 0).assertEqualTo(Choice(0, null))
		array(one.bit, 1).assertEqualTo(Choice(null, 1))
	}

	@Test
	fun get() {
		Choice(0, 1).get(zero.bit).assertEqualTo(0)
		Choice(0, 1).get(one.bit).assertEqualTo(1)
	}

	@Test
	fun set() {
		Choice(0, 1).set(zero.bit, 2).assertEqualTo(Choice(2, 1))
		Choice(0, 1).set(one.bit, 2).assertEqualTo(Choice(0, 2))
	}

	@Test
	fun plus() {
		Choice(0, null).plus(zero.bit, 2).assertEqualTo(null)
		Choice(0, null).plus(one.bit, 2).assertEqualTo(Choice(0, 2))
		Choice(null, 1).plus(zero.bit, 2).assertEqualTo(Choice(2, 1))
		Choice(null, 1).plus(one.bit, 2).assertEqualTo(null)
	}
}