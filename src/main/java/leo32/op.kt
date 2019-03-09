package leo32

sealed class Op {
	abstract fun invoke(arg: Int, runtime: Runtime): Runtime?
}

data class LogOp(val tag: String) : Op() {
	override fun invoke(arg: Int, runtime: Runtime) =
		runtime.invokeLog(tag)
}

object PushOp : Op() {
	override fun invoke(arg: Int, runtime: Runtime) =
		runtime.invokePush(arg)
}

object PopOp : Op() {
	override fun invoke(arg: Int, runtime: Runtime) =
		runtime.invokePop
}

object PlusOp : Op() {
	override fun invoke(arg: Int, runtime: Runtime) =
		runtime.invokePlus(arg)
}

