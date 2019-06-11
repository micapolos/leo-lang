package leo5.asm

data class Output(val fn: (Int) -> Unit)

fun output(fn: (Int) -> Unit) = Output(fn)
fun Output.put(int: Int) {
	fn(int)
}
