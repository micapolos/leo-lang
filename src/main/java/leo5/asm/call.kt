package leo5.asm

data class Call(val int: Int, val ret: Ret)

fun call(int: Int, ret: Ret) = Call(int, ret)

fun Call.invoke(runtime: Runtime) {
	runtime.memory.set(ret.ptr, runtime.pc.int)
	runtime.pc.int = int
}
