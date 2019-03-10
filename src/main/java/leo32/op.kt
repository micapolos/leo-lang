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

data class AndOp(val and: And) : Op() {
	override fun invoke(bit: Bit, runtime: Runtime) =
		runtime.and(bit)
}

data class OrOp(val or: Or) : Op() {
	override fun invoke(bit: Bit, runtime: Runtime) =
		runtime.or(bit)
}

data class NotOp(val not: Not) : Op() {
	override fun invoke(bit: Bit, runtime: Runtime) =
		runtime.not(bit)
}

val Log.op get() = LogOp(this)
val Push.op get() = PushOp(this)
val Pop.op get() = PopOp(this)
val And.op get() = AndOp(this)
val Or.op get() = OrOp(this)
val Not.op get() = NotOp(this)
