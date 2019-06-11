package leo5.asm

data class IntAdd(val lhs: Ptr, val rhs: Ptr)

fun intAdd(lhs: Ptr, rhs: Ptr) = IntAdd(lhs, rhs)

fun IntAdd.invoke(runtime: Runtime) {
	runtime.memory.intOp2(lhs, rhs, Int::plus)
}