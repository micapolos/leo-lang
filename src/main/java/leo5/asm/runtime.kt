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
		step()
	} while (!exit)
}

fun Runtime.step() {
	fetch().invoke(this)
}

fun Runtime.fetch() = code.ops[pc.int++]
