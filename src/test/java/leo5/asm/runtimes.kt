package leo5.asm

fun test(memory: Memory, code: Code, input: Input, expectedOutput: ExpectedOutput) {
	assertOutputs(expectedOutput.array) { output ->
		Runtime(memory, code, 0, input, output).run()
	}
}

data class ExpectedOutput(@Suppress("ArrayInDataClass") val array: IntArray)

fun expectedOutput(vararg ints: Int) = ExpectedOutput(ints)
