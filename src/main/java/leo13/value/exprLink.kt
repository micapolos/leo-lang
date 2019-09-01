package leo13.value

import leo13.Scriptable
import leo13.script.plus

data class ExprLink(val lhs: Expr, val op: Op) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = "link"
	override val scriptableBody get() = lhs.scriptableBody.plus(op.scriptableLine)
}

fun link(lhs: Expr, operator: Op) = ExprLink(lhs, operator)
