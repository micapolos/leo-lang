package leo5.asm

data class Write(val ptr: Ptr)

fun write(ptr: Ptr) = Write(ptr)

fun Write.invoke(runtime: Runtime) {
	runtime.output.write(runtime.memory.int(ptr))
}
