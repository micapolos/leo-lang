package leo.base

import org.junit.Test

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
}