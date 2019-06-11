package leo5.asm

fun test(input: Input, code: Code, expectedOutput: ExpectedOutput) {
	assertOutputs(expectedOutput.array) { output ->
		Runtime(newMemory, code, 0, input, output).run()
	}
}

data class ExpectedOutput(@Suppress("ArrayInDataClass") val array: IntArray)

fun expectedOutput(vararg ints: Int) = ExpectedOutput(ints)
