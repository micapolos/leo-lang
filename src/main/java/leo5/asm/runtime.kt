package leo5.asm

data class Runtime(val memory: Memory, val code: Code, var pc: Int, var input: Input, var output: Output)

val newRuntime = Runtime(newMemory, newCode, 0, input { 0 }, output { })
fun Runtime.run() {
	while (pc != 0) {
		code.ops[pc--].invoke(this)
	}
}
