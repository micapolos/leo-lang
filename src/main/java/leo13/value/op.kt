package leo13.value

import leo13.*
import leo13.script.Script
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script

sealed class Op : LeoObject() {
	override fun toString() = scriptableLine.toString()
	override val scriptableName get() = "op"
	override val scriptableBody get() = script(opScriptableLine)
	abstract val opScriptableName: String
	abstract val opScriptableBody: Script
	val opScriptableLine: ScriptLine get() = opScriptableName lineTo opScriptableBody
}

data class LhsOp(val lhs: Lhs) : Op() {
	override fun toString() = super.toString()
	override val opScriptableName get() = lhs.scriptableName
	override val opScriptableBody get() = lhs.scriptableBody
}

data class RhsLineOp(val rhsLine: RhsLine) : Op() {
	override fun toString() = super.toString()
	override val opScriptableName get() = rhsLine.scriptableName
	override val opScriptableBody get() = rhsLine.scriptableBody
}

data class RhsOp(val rhs: Rhs) : Op() {
	override fun toString() = super.toString()
	override val opScriptableName get() = rhs.scriptableName
	override val opScriptableBody get() = rhs.scriptableBody
}

data class GetOp(val get: Get) : Op() {
	override fun toString() = super.toString()
	override val opScriptableName get() = get.scriptableName
	override val opScriptableBody get() = get.scriptableBody
}

data class SetOp(val set: Set) : Op() {
	override fun toString() = super.toString()
	override val opScriptableName get() = set.scriptableName
	override val opScriptableBody get() = set.scriptableBody
}

data class WrapOp(val wrap: Wrap) : Op() {
	override fun toString() = super.toString()
	override val opScriptableName get() = wrap.scriptableName
	override val opScriptableBody get() = wrap.scriptableBody
}

data class SwitchOp(val switch: Switch) : Op() {
	override fun toString() = super.toString()
	override val opScriptableName get() = switch.scriptableName
	override val opScriptableBody get() = switch.scriptableBody
}

data class LineOp(val line: ExprLine) : Op() {
	override fun toString() = super.toString()
	override val opScriptableName get() = line.scriptableName
	override val opScriptableBody get() = line.scriptableBody
}

data class CallOp(val call: Call) : Op() {
	override fun toString() = super.toString()
	override val opScriptableName get() = call.scriptableName
	override val opScriptableBody get() = call.scriptableBody
}

data class BindOp(val bind: Bind) : Op() {
	override fun toString() = super.toString()
	override val opScriptableName get() = bind.scriptableName
	override val opScriptableBody get() = bind.scriptableBody
}

fun op(lhs: Lhs): Op = LhsOp(lhs)
fun op(rhs: Rhs): Op = RhsOp(rhs)
fun op(rhsLine: RhsLine): Op = RhsLineOp(rhsLine)
fun op(get: Get): Op = GetOp(get)
fun op(set: Set): Op = SetOp(set)
fun op(switch: Switch): Op = SwitchOp(switch)
fun op(line: ExprLine): Op = LineOp(line)
fun op(call: Call): Op = CallOp(call)
fun op(bind: Bind): Op = BindOp(bind)

val Op.lineOrNull get() = (this as? LineOp)?.line
