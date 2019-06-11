package leo5.asm

import kotlin.test.Test
import kotlin.test.assertFails

class RuntimeTest {
	@Test
	fun missingExit() {
		assertFails {
			test(
				memory(intSize(0)),
				code(),
				input(),
				expectedOutput())
		}
	}

	@Test
	fun exit() {
		test(
			memory(intSize(0)),
			code(op(exit)),
			input(),
			expectedOutput())
	}

	@Test
	fun readWrite() {
		test(
			memory(intSize(4)),
			code(
				op(read(intPtr(0))),
				op(write(intPtr(0))),
				op(exit)),
			input(10),
			expectedOutput(10))
	}

	@Test
	fun intSetConst() {
		test(
			memory(intSize(1)),
			code(
				op(set(intPtr(0), 123)),
				op(write(intPtr(0))),
				op(exit)),
			input(),
			expectedOutput(123))
	}

	@Test
	fun intSet() {
		test(
			memory(intSize(2)),
			code(
				op(read(intPtr(0))),
				op(intSet(intPtr(1), intPtr(0))),
				op(write(intPtr(1))),
				op(exit)),
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
				op(write(intPtr(0))),
				op(exit)),
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
				op(write(intPtr(0))),
				op(exit)),
			input(1, 2),
			expectedOutput(3))
	}

	@Test
	fun jump() {
		test(
			memory(intSize(1)),
			code(
				op(jump(2)),
				op(write(intPtr(0))),
				op(exit)),
			input(),
			expectedOutput())
	}

	@Test
	fun ifZeroJump() {
		test(
			memory(intSize(1)),
			code(
				op(read(intPtr(0))),
				op(ifZero(intPtr(0), jump(3))),
				op(write(intPtr(0))),
				op(exit)),
			input(0),
			expectedOutput())

		test(
			memory(intSize(1)),
			code(
				op(read(intPtr(0))),
				op(ifZero(intPtr(0), jump(3))),
				op(write(intPtr(0))),
				op(exit)),
			input(1),
			expectedOutput(1))
	}

	@Test
	fun branch() {
		test(
			memory(intSize(1)),
			code(
				op(read(intPtr(0))),
				op(branch(intPtr(0), table(2, 3))),
				op(write(intPtr(0))),
				op(write(intPtr(0))),
				op(exit)),
			input(0),
			expectedOutput(0, 0))

		test(
			memory(intSize(1)),
			code(
				op(read(intPtr(0))),
				op(branch(intPtr(0), table(2, 3))),
				op(write(intPtr(0))),
				op(write(intPtr(0))),
				op(exit)),
			input(1),
			expectedOutput(1))

		assertFails {
			test(
				memory(intSize(1)),
				code(
					op(read(intPtr(0))),
					op(branch(intPtr(0), table(2, 3))),
					op(write(intPtr(0))),
					op(write(intPtr(0))),
					op(exit)),
				input(2),
				expectedOutput())
		}
	}
}
