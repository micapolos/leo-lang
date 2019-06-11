package leo5.asm

data class IntInc(val ptr: Ptr)

fun intInc(ptr: Ptr) = IntInc(ptr)

fun IntInc.invoke(runtime: Runtime) {
	runtime.memory.intOp1(ptr, Int::inc)
}
