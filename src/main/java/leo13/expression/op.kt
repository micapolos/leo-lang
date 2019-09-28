package leo13.expression

import leo13.Given
import leo13.script.ScriptLine
import leo13.scriptLine
import leo13.value.Value
import leo13.value.scriptLine

sealed class Op

data class ValueOp(val value: Value) : Op()
data class EqualsOp(val equals: Equals) : Op()
data class WrapOp(val wrap: Wrap) : Op()
data class PlusOp(val plus: Plus) : Op()
data class GetOp(val get: Get) : Op()
data class SetOp(val set: Set) : Op()
data class PreviousOp(val previous: Previous) : Op()
data class ContentOp(val content: Content) : Op()
data class SwitchOp(val switch: Switch) : Op()
data class SwitchedOp(val switched: Switched) : Op()
data class GiveOp(val give: Give) : Op()
data class GivenOp(val given: Given) : Op()
data class ApplyOp(val apply: Apply) : Op()
data class FixOp(val fix: Fix) : Op()

val Value.op: Op get() = ValueOp(this)
val Wrap.op: Op get() = WrapOp(this)
val Equals.op: Op get() = EqualsOp(this)
val Plus.op: Op get() = PlusOp(this)
val Get.op: Op get() = GetOp(this)
val Set.op: Op get() = SetOp(this)
val Previous.op: Op get() = PreviousOp(this)
val Content.op: Op get() = ContentOp(this)
val Switch.op: Op get() = SwitchOp(this)
val Switched.op: Op get() = SwitchedOp(this)
val Give.op: Op get() = GiveOp(this)
val Given.op: Op get() = GivenOp(this)
val Apply.op: Op get() = ApplyOp(this)
val Fix.op: Op get() = FixOp(this)

fun op(value: Value) = value.op
fun op(equals: Equals) = equals.op
fun op(wrap: Wrap) = wrap.op
fun op(plus: Plus) = plus.op
fun op(get: Get) = get.op
fun op(set: Set) = set.op
fun op(previous: Previous) = previous.op
fun op(content: Content) = content.op
fun op(switch: Switch) = switch.op
fun op(switched: Switched) = switched.op
fun op(give: Give) = give.op
fun op(given: Given) = given.op
fun op(apply: Apply) = apply.op
fun op(fix: Fix) = fix.op

val Op.bodyScriptLine: ScriptLine
	get() =
		when (this) {
			is ValueOp -> value.scriptLine
			is EqualsOp -> equals.scriptingLine
			is WrapOp -> wrap.scriptingLine
			is PlusOp -> plus.line.bodyScriptLine // TODO: escape with meta!!!
			is GetOp -> get.scriptLine
			is SetOp -> set.scriptLine
			is PreviousOp -> previous.scriptLine
			is ContentOp -> content.scriptLine
			is SwitchOp -> switch.scriptingLine
			is SwitchedOp -> switched.scriptLine
			is GiveOp -> give.scriptLine
			is GivenOp -> given.scriptLine
			is ApplyOp -> apply.scriptLine
			is FixOp -> fix.scriptingLine
		}