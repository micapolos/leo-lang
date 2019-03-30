package leo32

import leo.base.assertEqualTo
import leo.base.stack
import leo.base.write
import leo.base.writeStackOrNull
import leo.binary.Bit
import leo.binary.bit
import leo.binary.one
import leo.binary.zero
import kotlin.test.Test

class ByteToBitTest {
	@Test
	fun emptyWriter() {
		writeStackOrNull<Bit> { byteToBit }
			.assertEqualTo(null)
	}

	@Test
	fun oneWriter() {
		writeStackOrNull<Bit> { byteToBit.write(32) }
			.assertEqualTo(stack(zero.bit, zero.bit, one.bit, zero.bit, zero.bit, zero.bit, zero.bit, zero.bit))
	}

	@Test
	fun manyWriter() {
		writeStackOrNull<Bit> {
			byteToBit
				.write(32)
				.write(33)
		}.assertEqualTo(
			stack(
				zero.bit, zero.bit, one.bit, zero.bit, zero.bit, zero.bit, zero.bit, zero.bit,
				zero.bit, zero.bit, one.bit, zero.bit, zero.bit, zero.bit, zero.bit, one.bit))
	}
}