package leo5.asm

fun test(memory: Memory, code: Code, input: Input, expectedOutput: ExpectedOutput) {
	assertOutputs(expectedOutput.array) { output ->
		Runtime(memory, code, pc(0), lhs(base(ptr(0))), rhs(base(ptr(0))), input, output).run()
	}
}

data class ExpectedOutput(@Suppress("ArrayInDataClass") val array: IntArray)

fun expectedOutput(vararg ints: Int) = ExpectedOutput(ints)
