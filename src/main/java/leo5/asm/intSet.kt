package leo5.asm

data class IntSet(val lhs: Ptr, val rhs: Ptr)

fun intSet(lhs: Ptr, rhs: Ptr) = IntSet(lhs, rhs)

fun IntSet.invoke(runtime: Runtime) {
	runtime.memory.intOp2(lhs, rhs) { it }
}