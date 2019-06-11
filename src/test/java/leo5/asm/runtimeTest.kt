package leo5.asm

import kotlin.test.Test

class RuntimeTest {
	@Test
	fun readWrite() {
		test(
			input(10),
			code(op(read(0)), op(write(0))),
			expectedOutput(10))
	}

	@Test
	fun intSet() {
		test(
			input(10),
			code(op(read(0)), op(intSet(1, 0)), op(write(1))),
			expectedOutput(10))
	}

	@Test
	fun intInc() {
		test(
			input(1),
			code(op(read(0)), op(intInc(0)), op(write(0))),
			expectedOutput(2))
	}

	@Test
	fun intAdd() {
		test(
			input(1, 2),
			code(op(read(0)), op(read(4)), op(intAdd(0, 4)), op(write(0))),
			expectedOutput(3))
	}
}
