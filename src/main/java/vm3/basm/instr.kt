package vm3.basm

import vm3.asm.Prim

sealed class Instr
data class PrimInstr(val prim: Prim) : Instr()
data class CallInstr(val call: Call) : Instr()
data class SwitchInstr(val switch: Switch) : Instr()

data class Block(val instrList: List<Instr>)
data class Procedure(val block: Block, val returnAddress: Int)

data class Call(val procedure: Procedure)
data class Switch(val cond: Int, val procedureList: List<Procedure>)

fun block(vararg instructions: Instr) = Block(instructions.toList())