package leo13.untyped.expression

import leo13.script.ScriptLine

sealed class Op

data class ConstantOp(val constant: Constant) : Op()
data class GetOp(val get: Get) : Op()
data class SetOp(val set: Set) : Op()
data class PreviousOp(val previous: Previous) : Op()
data class SwitchOp(val switch: Switch) : Op()
data class BindOp(val bind: Bind) : Op()
data class BoundOp(val bound: Bound) : Op()
data class ApplyOp(val apply: Apply) : Op()

val Constant.op: Op get() = ConstantOp(this)
val Get.op: Op get() = GetOp(this)
val Set.op: Op get() = SetOp(this)
val Previous.op: Op get() = PreviousOp(this)
val Switch.op: Op get() = SwitchOp(this)
val Bind.op: Op get() = BindOp(this)
val Bound.op: Op get() = BoundOp(this)
val Apply.op: Op get() = ApplyOp(this)

val Op.bodyScriptLine: ScriptLine
	get() =
		when (this) {
			is ConstantOp -> constant.scriptLine
			is GetOp -> get.scriptLine
			is SetOp -> set.scriptLine
			is PreviousOp -> previous.scriptLine
			is SwitchOp -> switch.scriptLine
			is BindOp -> bind.scriptLine
			is BoundOp -> bound.scriptLine
			is ApplyOp -> apply.scriptLine

		}