package leo5.asm

data class Write(val index: Int)

fun write(index: Int) = Write(index)
fun Write.invoke(runtime: Runtime) {
	runtime.output.put(runtime.memory.int(index))
}
