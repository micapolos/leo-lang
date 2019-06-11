package leo5.asm

data class IntSet(val index: Int, val argIndex: Int)

fun intSet(index: Int, argIndex: Int) = IntSet(index, argIndex)
fun IntSet.invoke(runtime: Runtime) {
	runtime.memory.intOp2(index, argIndex) { it }
}