package leo13.value

import leo13.Lhs
import leo13.Rhs
import leo13.RhsLine
import leo13.Wrap
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script

sealed class Op {
	override fun toString() = asScriptLine.toString()
	val asScriptLine get() = "op" lineTo script(opAsScriptLine)
	abstract val opAsScriptLine: ScriptLine
}

data class ArgumentOp(val given: Given) : Op() {
	override fun toString() = super.toString()
	override val opAsScriptLine = given.scriptableLine
}

data class LhsOp(val lhs: Lhs) : Op() {
	override fun toString() = super.toString()
	override val opAsScriptLine = lhs.scriptableLine
}

data class RhsLineOp(val rhsLine: RhsLine) : Op() {
	override fun toString() = super.toString()
	override val opAsScriptLine = rhsLine.scriptableLine
}

data class RhsOp(val rhs: Rhs) : Op() {
	override fun toString() = super.toString()
	override val opAsScriptLine = rhs.scriptableLine
}

data class GetOp(val get: Get) : Op() {
	override fun toString() = super.toString()
	override val opAsScriptLine = get.scriptableLine
}

data class WrapOp(val wrap: Wrap) : Op() {
	override fun toString() = super.toString()
	override val opAsScriptLine = wrap.scriptableLine
}

data class SwitchOp(val switch: Switch) : Op() {
	override fun toString() = super.toString()
	override val opAsScriptLine = switch.scriptableLine
}

data class LineOp(val line: ExprLine) : Op() {
	override fun toString() = super.toString()
	override val opAsScriptLine = line.scriptableLine
}

data class CallOp(val call: Call) : Op() {
	override fun toString() = super.toString()
	override val opAsScriptLine = call.asScriptLine
}

fun op(given: Given): Op = ArgumentOp(given)
fun op(lhs: Lhs): Op = LhsOp(lhs)
fun op(rhs: Rhs): Op = RhsOp(rhs)
fun op(rhsLine: RhsLine): Op = RhsLineOp(rhsLine)
fun op(get: Get): Op = GetOp(get)
fun op(switch: Switch): Op = SwitchOp(switch)
fun op(line: ExprLine): Op = LineOp(line)
fun op(call: Call): Op = CallOp(call)

val Op.lineOrNull get() = (this as? LineOp)?.line
