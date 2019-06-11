package leo5.asm

import kotlin.test.Test

class RuntimeTest {
	@Test
	fun readWrite() {
		test(
			memory(size(4)),
			code(op(read(0)), op(write(0))),
			input(10),
			expectedOutput(10))
	}

	@Test
	fun intSet() {
		test(
			memory(size(8)),
			code(op(read(0)), op(intSet(4, 0)), op(write(4))),
			input(10),
			expectedOutput(10))
	}

	@Test
	fun intInc() {
		test(
			memory(size(4)),
			code(op(read(0)), op(intInc(0)), op(write(0))),
			input(1),
			expectedOutput(2))
	}

	@Test
	fun intAdd() {
		test(
			memory(size(8)),
			code(op(read(0)), op(read(4)), op(intAdd(0, 4)), op(write(0))),
			input(1, 2),
			expectedOutput(3))
	}
}
