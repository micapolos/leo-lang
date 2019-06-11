package leo5.asm

sealed class Op

data class ExitOp(val exit: Exit) : Op()
data class IntPutOp(val intPut: IntPut) : Op()
data class IntIncOp(val intInc: IntInc) : Op()
data class IntAddOp(val intAdd: IntAdd) : Op()
data class BranchOp(val branch: Branch) : Op()
data class ReadOp(val read: Read) : Op()
data class WriteOp(val write: Write) : Op()

fun op(exit: Exit): Op = ExitOp(exit)
fun op(intPut: IntPut): Op = IntPutOp(intPut)
fun op(intInc: IntInc): Op = IntIncOp(intInc)
fun op(intAdd: IntAdd): Op = IntAddOp(intAdd)
fun op(branch: Branch): Op = BranchOp(branch)
fun op(read: Read): Op = ReadOp(read)
fun op(write: Write): Op = WriteOp(write)

fun Op.invoke(runtime: Runtime) = when (this) {
	is ExitOp -> exit.invoke(runtime)
	is IntPutOp -> intPut.invoke(runtime)
	is IntIncOp -> intInc.invoke(runtime)
	is IntAddOp -> intAdd.invoke(runtime)
	is BranchOp -> branch.invoke(runtime)
	is ReadOp -> read.invoke(runtime)
	is WriteOp -> write.invoke(runtime)
}
