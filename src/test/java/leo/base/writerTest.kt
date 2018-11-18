package leo.base

import org.junit.Test

class WriterTest {
	@Test
	fun testPrintlnWriter() {
		printlnWriter<Bit>().write(stream(Bit.ZERO, Bit.ONE))
	}

	@Test
	fun stackWriter() {
		nullStack<Int>()
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
				.write(Bit.ZERO)
				.write(Bit.ZERO)
				.write(Bit.ZERO)
				.write(Bit.ZERO)
				.write(Bit.ONE)
				.write(Bit.ONE)
				.write(Bit.ZERO)
				.write(Bit.ONE)
		}
			.assertEqualTo(stack(13.byte))
	}
}