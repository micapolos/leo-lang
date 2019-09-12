package leo13.untyped.expression

sealed class Op

data class ConstantOp(val constant: Constant) : Op()
data class GetOp(val get: Get) : Op()
data class SetOp(val set: Set) : Op()
data class PreviousOp(val previous: Previous) : Op()
data class SwitchOp(val switch: Switch) : Op()

val Constant.op: Op get() = ConstantOp(this)
val Get.op: Op get() = GetOp(this)
val Set.op: Op get() = SetOp(this)
val Previous.op: Op get() = PreviousOp(this)
val Switch.op: Op get() = SwitchOp(this)
