package leo13.untyped.expression

import leo13.script.ScriptLine

sealed class Op

data class PlusOp(val plus: Plus) : Op()
data class GetOp(val get: Get) : Op()
data class SetOp(val set: Set) : Op()
data class PreviousOp(val previous: Previous) : Op()
data class ContentOp(val content: Content) : Op()
data class SwitchOp(val switch: Switch) : Op()
data class GiveOp(val give: Give) : Op()
data class GivenOp(val given: Given) : Op()
data class ApplyOp(val apply: Apply) : Op()

val Plus.op: Op get() = PlusOp(this)
val Get.op: Op get() = GetOp(this)
val Set.op: Op get() = SetOp(this)
val Previous.op: Op get() = PreviousOp(this)
val Content.op: Op get() = ContentOp(this)
val Switch.op: Op get() = SwitchOp(this)
val Give.op: Op get() = GiveOp(this)
val Given.op: Op get() = GivenOp(this)
val Apply.op: Op get() = ApplyOp(this)

val Op.bodyScriptLine: ScriptLine
	get() =
		when (this) {
			is PlusOp -> plus.scriptLine
			is GetOp -> get.scriptLine
			is SetOp -> set.scriptLine
			is PreviousOp -> previous.scriptLine
			is ContentOp -> content.scriptLine
			is SwitchOp -> switch.scriptLine
			is GiveOp -> give.scriptLine
			is GivenOp -> given.scriptLine
			is ApplyOp -> apply.scriptLine
		}