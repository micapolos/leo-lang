package vm3.basm.asm

import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toImmutableList
import leo.base.fold
import leo.base.seq
import leo13.Stack
import leo13.push
import leo13.stack
import vm3.asm.Op
import vm3.asm.Prim
import vm3.asm.op
import vm3.basm.Block
import vm3.basm.Call
import vm3.basm.CallInstr
import vm3.basm.Instr
import vm3.basm.PrimInstr
import vm3.basm.Procedure
import vm3.basm.Switch
import vm3.basm.SwitchInstr

data class Compiler(
	val opStack: Stack<Op>,
	val opCount: Int,
	val procedureAddressMap: PersistentMap<Procedure, Int>
)

val emptyCompiler = Compiler(stack(), 0, persistentMapOf())

fun Compiler.add(procedure: Procedure): Compiler =
	procedureAddressMap[procedure].let { addressOrNull ->
		if (addressOrNull == null) addMissing(procedure)
		else this
	}

fun Compiler.addMissing(procedure: Procedure): Compiler = this
	.addProcedures(procedure.block)
	.addAddress(procedure)
	.add(procedure.block)
	.add(Op.Ret(procedure.returnAddress))

fun Compiler.addProcedures(instr: Instr): Compiler =
	when (instr) {
		is PrimInstr -> this
		is CallInstr -> add(instr.call.procedure)
		is SwitchInstr -> fold(instr.switch.procedureList.seq) { add(it) }
	}

fun Compiler.addProcedures(block: Block): Compiler =
	fold(block.instrList.seq) { addProcedures(it) }

fun Compiler.add(block: Block): Compiler =
	fold(block.instrList.seq) { add(it) }

fun Compiler.add(instr: Instr): Compiler =
	when (instr) {
		is PrimInstr -> add(instr.prim)
		is CallInstr -> add(instr.call)
		is SwitchInstr -> add(instr.switch)
	}

fun Compiler.add(prim: Prim): Compiler =
	add(prim.op)

fun Compiler.add(call: Call): Compiler =
	add(
		Op.Call(
			procedureAddress(call.procedure),
			call.procedure.returnAddress))

fun Compiler.add(switch: Switch): Compiler =
	add(
		Op.CallTable(
			switch.cond,
			switch.procedureList
				.map { Op.Call(procedureAddress(it), it.returnAddress) }
				.toImmutableList()))

fun Compiler.add(op: Op) =
	copy(
		opStack = opStack.push(op),
		opCount = opCount.inc())

fun Compiler.procedureAddress(procedure: Procedure): Int =
	procedureAddressMap[procedure]!!

fun Compiler.addAddress(procedure: Procedure): Compiler =
	copy(procedureAddressMap = procedureAddressMap.put(procedure, opCount))
