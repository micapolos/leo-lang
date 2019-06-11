package leo5.asm

data class Branch(val ptr: Ptr, val table: Table)

fun branch(ptr: Ptr, table: Table) = Branch(ptr, table)

fun Branch.invoke(runtime: Runtime) {
	runtime.pc.int = table[runtime.memory.int(ptr)]
}
