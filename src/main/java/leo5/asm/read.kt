package leo5.asm

data class Read(val index: Int)

fun read(index: Int) = Read(index)
fun Read.invoke(runtime: Runtime) {
	runtime.memory.put(index, runtime.input.int)
}
