package vm2

import java.nio.ByteBuffer
import java.util.*

data class Context(
	val memory: ByteBuffer,
	val stack: ArrayDeque<Numeric>
)

fun Context.push(numeric: Numeric) {
	stack.push(numeric)
}

fun <N : Numeric> Context.pop(): N = stack.pop() as N

fun Context.execute(instruction: Instruction): Unit =
	when (instruction) {
		is ConstantInstruction -> push(instruction.numeric)
		is IntegerUnaryNumericInstruction -> when (instruction.op) {
			IntegerCountLeadingZerosOp ->
				when (instruction.size) {
					Size32 -> push(pop<I32>().leadingZerosCount)
					Size64 -> push(pop<I64>().leadingZerosCount)
				}
			IntegerCountTrailingZerosOp ->
				when (instruction.size) {
					Size32 -> push(pop<I32>().trailingZerosCount)
					Size64 -> push(pop<I64>().trailingZerosCount)
				}
			IntegerCountOnes ->
				when (instruction.size) {
					Size32 -> push(pop<I32>().onesCount)
					Size64 -> push(pop<I64>().onesCount)
				}
		}
		is FloatingPointUnaryNumericInstruction -> TODO()
		is IntegerBinaryNumericInstruction -> TODO()
		is FloatingPointBinaryNumericInstruction -> TODO()
		is IntegerTestNumericInstruction -> TODO()
		is IntegerComparizonNumericInstruction -> TODO()
		is FloatingPointComparisonNumericInstruction -> TODO()
	}