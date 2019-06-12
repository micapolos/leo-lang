package leo5.asm

data class Ret(val ptr: Ptr)

fun ret(ptr: Ptr) = Ret(ptr)

fun Ret.invoke(runtime: Runtime) {
	runtime.pc.int = runtime.memory.int(ptr)
}
