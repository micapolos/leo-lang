package leo5.asm

data class Branch(val ptr: Ptr, val jumpTable: JumpTable)

fun branch(ptr: Ptr, jumpTable: JumpTable) = Branch(ptr, jumpTable)

fun Branch.invoke(runtime: Runtime) {
	runtime.pc.int = jumpTable[runtime.memory.int(ptr)]
}
