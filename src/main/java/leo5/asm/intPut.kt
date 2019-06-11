package leo5.asm

data class IntPut(val index: Int, val int: Int)

fun put(index: Int, int: Int) = IntPut(index, int)
fun IntPut.invoke(runtime: Runtime) {
	runtime.memory.put(index, int)
}
