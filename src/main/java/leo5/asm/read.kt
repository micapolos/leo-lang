package leo5.asm

data class Read(val ptr: Ptr)

fun read(ptr: Ptr) = Read(ptr)

fun Read.invoke(runtime: Runtime) {
	runtime.memory.set(ptr, runtime.input.read)
}
