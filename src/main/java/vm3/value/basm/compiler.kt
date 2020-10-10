package vm3.value.basm

import kotlinx.collections.immutable.PersistentMap
import leo13.Stack
import vm3.basm.Instr
import vm3.basm.Procedure
import vm3.value.Value

data class Compiler(
	val instructions: Stack<Instr>,
	val procedureMap: PersistentMap<Value.Function, Procedure>
)

