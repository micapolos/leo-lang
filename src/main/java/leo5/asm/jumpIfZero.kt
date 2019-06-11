package leo5.asm

data class IfZeroJump(val ptr: Ptr, val jump: Jump)

fun ifZero(ptr: Ptr, jump: Jump) = IfZeroJump(ptr, jump)

fun IfZeroJump.invoke(runtime: Runtime) {
	if (runtime.memory.int(ptr) == 0) jump.invoke(runtime)
}