package leo32

sealed class Match {
	abstract fun invoke(arg: Int, runtime: Runtime): Runtime?
}

val pushMatch = OpMatch(PushOp)

data class PartialMatch(
	val function: Function) : Match() {
	override fun invoke(arg: Int, runtime: Runtime) =
		runtime.push(function)
}

data class OpMatch(
	val op: Op) : Match() {
	override fun invoke(arg: Int, runtime: Runtime) =
		op.invoke(arg, runtime)
}
