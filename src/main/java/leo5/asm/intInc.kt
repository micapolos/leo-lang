package leo5.asm

data class IntInc(val index: Int)

fun intInc(index: Int) = IntInc(index)
fun IntInc.invoke(runtime: Runtime) {
	runtime.memory.intOp1(index, Int::inc)
}
