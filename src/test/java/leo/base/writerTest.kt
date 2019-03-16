package leo.base

import kotlin.test.Test

class WriterTest {
	@Test
	fun testPrintlnWriter() {
		printlnWriter<EnumBit>().assertNotNull
	}

	@Test
	fun stackWriter() {
		nullOf<Stack<Int>>()
			.writerFold(Stack<Int>?::push) { write(1).write(2) }
			.assertEqualTo(stack(1, 2))
	}

	@Test
	fun test_writeStackOrNull() {
		writeStackOrNull<Int> { write(1).write(2) }.assertEqualTo(stack(1, 2))
	}

	@Test
	fun byteBitWriter() {
		writeStackOrNull<Byte> {
			byteBitWriter
				.write(EnumBit.ZERO)
				.write(EnumBit.ZERO)
				.write(EnumBit.ZERO)
				.write(EnumBit.ZERO)
				.write(EnumBit.ONE)
				.write(EnumBit.ONE)
				.write(EnumBit.ZERO)
				.write(EnumBit.ONE)
		}
			.assertEqualTo(stack(13.clampedByte))
	}

	@Test
	fun split() {
		writeStackOrNull<Int> {
			this
				.split<Int, Int>({ it / 2 }, { it - it / 2 })
				.write(12)
				.write(13)
				.write(14)
		}.assertEqualTo(stack(6, 6, 6, 7, 7, 7))
	}

	@Test
	fun join() {
		writeStackOrNull<Int> {
			this
				.join<Int, Int> { a, b -> a + b }
				.write(1)
				.write(2)
				.write(3)
				.write(4)
				.write(5)
		}.assertEqualTo(stack(3, 7))
	}
}