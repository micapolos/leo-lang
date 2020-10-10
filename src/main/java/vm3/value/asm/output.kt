package vm3.value.asm

import leo13.Stack
import leo13.push
import vm3.asm.Op

data class Output(
	val dataSize: Int,
	val ops: Stack<Op>
)

fun Output.plusData(size: Int) = copy(dataSize = dataSize + size)
fun Output.plus(op: Op) = copy(ops = ops.push(op))
