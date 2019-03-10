package leo32

import leo.binary.Bit

sealed class Op {
	abstract fun invoke(bit: Bit, runtime: Runtime): Runtime?
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
		runtime.nand(bit)
}

val Log.op get() = LogOp(this)
val Push.op get() = PushOp(this)
val Pop.op get() = PopOp(this)
val Nand.op get() = NandOp(this)
