package leo5.asm

data class Runtime(
	val memory: Memory,
	val code: Code,
	val pc: Pc,
	val input: Input,
	val output: Output)

fun Runtime.run() {
	while (pc.int != code.ops.size) {
		code.ops[pc.int++].invoke(this)
	}
}
