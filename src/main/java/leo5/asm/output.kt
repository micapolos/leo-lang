package leo5.asm

data class Output(val fn: (Int) -> Unit)

fun output(fn: (Int) -> Unit) = Output(fn)

fun Output.write(int: Int) {
	fn(int)
}
