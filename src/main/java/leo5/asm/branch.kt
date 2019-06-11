package leo5.asm

data class Branch(val index: Int, val table: Table)

fun branch(index: Int, table: Table) = Branch(index, table)
fun Branch.invoke(runtime: Runtime) {
	runtime.pc = table[runtime.memory.int(index)]
}
