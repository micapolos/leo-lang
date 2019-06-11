package leo5.asm

data class IntSetConst(val lhs: Ptr, val int: Int)

fun set(lhs: Ptr, int: Int) = IntSetConst(lhs, int)

fun IntSetConst.invoke(runtime: Runtime) {
	runtime.memory.set(lhs, int)
}
