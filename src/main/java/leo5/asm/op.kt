package leo5.asm

sealed class Op

data class ExitOp(val exit: Exit) : Op()
data class IntPutOp(val intSetConst: IntSetConst) : Op()
data class IntSetOp(val intSet: IntSet) : Op()
data class IntIncOp(val intInc: IntInc) : Op()
data class IntAddOp(val intAdd: IntAdd) : Op()
data class JumpOp(val jump: Jump) : Op()
data class JumpIfOp(val conditionalJump: ConditionalJump) : Op()
data class BranchOp(val branch: Branch) : Op()
data class ReadOp(val read: Read) : Op()
data class WriteOp(val write: Write) : Op()

fun op(exit: Exit): Op = ExitOp(exit)
fun op(intSetConst: IntSetConst): Op = IntPutOp(intSetConst)
fun op(intSet: IntSet): Op = IntSetOp(intSet)
fun op(intInc: IntInc): Op = IntIncOp(intInc)
fun op(intAdd: IntAdd): Op = IntAddOp(intAdd)
fun op(jump: Jump): Op = JumpOp(jump)
fun op(conditionalJump: ConditionalJump): Op = JumpIfOp(conditionalJump)
fun op(branch: Branch): Op = BranchOp(branch)
fun op(read: Read): Op = ReadOp(read)
fun op(write: Write): Op = WriteOp(write)

fun Op.invoke(runtime: Runtime) = when (this) {
	is ExitOp -> exit.invoke(runtime)
	is IntPutOp -> intSetConst.invoke(runtime)
	is IntSetOp -> intSet.invoke(runtime)
	is IntIncOp -> intInc.invoke(runtime)
	is IntAddOp -> intAdd.invoke(runtime)
	is JumpOp -> jump.invoke(runtime)
	is JumpIfOp -> conditionalJump.invoke(runtime)
	is BranchOp -> branch.invoke(runtime)
	is ReadOp -> read.invoke(runtime)
	is WriteOp -> write.invoke(runtime)
}
