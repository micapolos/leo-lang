package leo5.asm

import kotlin.test.Test

class RuntimeTest {
	@Test
	fun readWrite() {
		test(
			memory(intSize(4)),
			code(
				op(read(intPtr(0))),
				op(write(intPtr(0)))),
			input(10),
			expectedOutput(10))
	}

	@Test
	fun intSet() {
		test(
			memory(intSize(2)),
			code(
				op(read(intPtr(0))),
				op(intSet(intPtr(1), intPtr(0))),
				op(write(intPtr(1)))),
			input(10),
			expectedOutput(10))
	}

	@Test
	fun intInc() {
		test(
			memory(intSize(1)),
			code(
				op(read(intPtr(0))),
				op(intInc(intPtr(0))),
				op(write(intPtr(0)))),
			input(1),
			expectedOutput(2))
	}

	@Test
	fun intAdd() {
		test(
			memory(intSize(2)),
			code(
				op(read(intPtr(0))),
				op(read(intPtr(1))),
				op(intAdd(intPtr(0), intPtr(1))),
				op(write(intPtr(0)))),
			input(1, 2),
			expectedOutput(3))
	}
}
