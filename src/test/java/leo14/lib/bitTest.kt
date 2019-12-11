package leo14.lib

import leo.base.assertEqualTo
import kotlin.test.Test

class BitTest {
	@Test
	fun negate() {
		bit0.negate.assertEqualTo(bit1)
		bit1.negate.assertEqualTo(bit0)
	}

	@Test
	fun and() {
		bit0.and(bit0).assertEqualTo(bit0)
		bit0.and(bit1).assertEqualTo(bit0)
		bit1.and(bit0).assertEqualTo(bit0)
		bit1.and(bit1).assertEqualTo(bit1)
	}

	@Test
	fun or() {
		bit0.or(bit0).assertEqualTo(bit0)
		bit0.or(bit1).assertEqualTo(bit1)
		bit1.or(bit0).assertEqualTo(bit1)
		bit1.or(bit1).assertEqualTo(bit1)
	}

	@Test
	fun xor() {
		bit0.xor(bit0).assertEqualTo(bit0)
		bit0.xor(bit1).assertEqualTo(bit1)
		bit1.xor(bit0).assertEqualTo(bit1)
		bit1.xor(bit1).assertEqualTo(bit0)
	}
}