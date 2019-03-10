package leo32

import leo.binary.Bit

sealed class Op {
	abstract fun invoke(bit: Bit, runtime: Runtime): Runtime
}

data class LogOp(val log: Log) : Op() {
	override fun invoke(bit: Bit, runtime: Runtime) =
		runtime.invoke(log)
}

data class PushOp(val push: Push) : Op() {
	override fun invoke(bit: Bit, runtime: Runtime) =
		runtime.push(bit)
}

data class PopOp(val pop: Pop) : Op() {
	override fun invoke(bit: Bit, runtime: Runtime) =
		runtime.pop
}

data class NandOp(val nand: Nand) : Op() {
	override fun invoke(bit: Bit, runtime: Runtime) =
		runtime.nand
}

data class SeqOp(
	val firstOp: Op,
	val secondOp: Op) : Op() {
	override fun invoke(bit: Bit, runtime: Runtime) =
		firstOp.invoke(bit, runtime).let {
			secondOp.invoke(bit, it)
		}
}

val Log.op: Op get() = LogOp(this)
val Push.op: Op get() = PushOp(this)
val Pop.op: Op get() = PopOp(this)
val Nand.op: Op get() = NandOp(this)
fun Op.then(op: Op) =
	SeqOp(this, op)
