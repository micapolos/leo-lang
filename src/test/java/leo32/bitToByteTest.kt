package leo32

import leo.base.*
import leo.binary.bit
import leo.binary.one
import leo.binary.zero
import kotlin.test.Test

class BitToByteTest {
	@Test
	fun partialWriter() {
		writeStackOrNull<Byte> {
			bitToByte
				.write(zero.bit)
				.write(zero.bit)
				.write(one.bit)
				.write(zero.bit)
				.write(zero.bit)
				.write(zero.bit)
				.write(zero.bit)
		}.assertEqualTo(null)
	}

	@Test
	fun oneWriter() {
		writeStackOrNull<Byte> {
			bitToByte
				.write(zero.bit)
				.write(zero.bit)
				.write(one.bit)
				.write(zero.bit)
				.write(zero.bit)
				.write(zero.bit)
				.write(zero.bit)
				.write(zero.bit)
		}.assertEqualTo(stack(32.clampedByte))
	}

	@Test
	fun manyWriter() {
		writeStackOrNull<Byte> {
			bitToByte
				.write(zero.bit)
				.write(zero.bit)
				.write(one.bit)
				.write(zero.bit)
				.write(zero.bit)
				.write(zero.bit)
				.write(zero.bit)
				.write(zero.bit)
				.write(zero.bit)
				.write(zero.bit)
				.write(one.bit)
				.write(zero.bit)
				.write(zero.bit)
				.write(zero.bit)
				.write(zero.bit)
				.write(one.bit)
		}.assertEqualTo(stack(32.clampedByte, 33.clampedByte))
	}
}