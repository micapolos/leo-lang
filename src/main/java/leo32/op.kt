package leo32

sealed class Op
data class LogOp(val tag: String) : Op()
object PushOp : Op()
object Add8Op : Op()
