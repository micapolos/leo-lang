package leo32

import leo.base.*
import leo.binary.bit
import leo.binary.one
import leo.binary.zero
import kotlin.test.Test

class BitToByteTest {
	@Test
	fun invoke() {
		initialBitToByte.assertEqualTo(BitToByte(0.clampedByte, 0x80.clampedByte))
		initialBitToByte.invoke(zero.bit).assertEqualTo(BitToByte(0.clampedByte, 0x40.clampedByte))
		initialBitToByte.invoke(one.bit).assertEqualTo(BitToByte(0x80.clampedByte, 0x40.clampedByte))
	}

	@Test
	fun partialWriter() {
		writeStackOrNull<Byte> {
			invoke(initialBitToByte)
				.write(zero.bit)
				.write(zero.bit)
				.write(one.bit)
				.write(zero.bit)
				.write(zero.bit)
				.write(zero.bit)
				.write(zero.bit)
		}.assertEqualTo(stack(32.clampedByte))
	}

	@Test
	fun oneWriter() {
		writeStackOrNull<Byte> {
			invoke(initialBitToByte)
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
			invoke(initialBitToByte)
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