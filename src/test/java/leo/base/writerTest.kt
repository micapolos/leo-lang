package leo.base

import org.junit.Test

class WriterTest {
	@Test
	fun testPrintlnWriter() {
		printlnWriter<Bit>().write(stream(Bit.ZERO, Bit.ONE))
	}

	@Test
	fun stackWriter() {
		nullStack<Int>().writer
			.write(1)
			.write(2)
			.written
			.assertEqualTo(stack(1, 2))
	}

	@Test
	fun bitByteWriter() {
		writerStackOrNull<Int> {
			write(1).write(2)
		}.assertEqualTo(stack(1, 2))
	}
}