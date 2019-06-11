package leo5.asm

data class Runtime(
	val memory: Memory,
	val code: Code,
	val pc: Pc,
	val input: Input,
	val output: Output,
	var exit: Boolean = false)

fun Runtime.run() {
	exit = false
	do {
		code.ops[pc.int++].invoke(this)
	} while (!exit)
}
