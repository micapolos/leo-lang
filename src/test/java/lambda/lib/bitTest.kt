package lambda.lib

import leo.base.assertEqualTo
import leo.base.assertFails
import leo.base.assertNotEqualTo
import leo.binary.bit0
import leo.binary.bit1
import kotlin.test.Test

class BitTest {
	@Test
	fun equality() {
		zero.assertEqualTo(zero)
		zero.assertNotEqualTo(one)
		one.assertNotEqualTo(zero)
		one.assertEqualTo(one)
	}

	@Test
	fun math() {
		zero.not.assertEqualTo(one)
		one.not.assertEqualTo(zero)

		zero.and(zero).assertEqualTo(zero)
		zero.and(one).assertEqualTo(zero)
		one.and(zero).assertEqualTo(zero)
		one.and(one).assertEqualTo(one)

		zero.or(zero).assertEqualTo(zero)
		zero.or(one).assertEqualTo(one)
		one.or(zero).assertEqualTo(one)
		one.or(one).assertEqualTo(one)

		zero.xor(zero).assertEqualTo(zero)
		zero.xor(one).assertEqualTo(one)
		one.xor(zero).assertEqualTo(one)
		one.xor(one).assertEqualTo(zero)
	}

	@Test
	fun reflection() {
		zero.bit.assertEqualTo(bit0)
		one.bit.assertEqualTo(bit1)
		assertFails { id.bit }

		bit0.term.assertEqualTo(zero)
		bit1.term.assertEqualTo(one)
	}
}