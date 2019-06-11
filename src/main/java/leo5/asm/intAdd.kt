package leo5.asm

data class IntAdd(val index: Int, val argIndex: Int)

fun intAdd(index: Int, argIndex: Int) = IntAdd(index, argIndex)

fun IntAdd.invoke(runtime: Runtime) {
	runtime.memory.intOp2(index, argIndex, Int::plus)
}