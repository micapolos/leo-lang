package leo13.value

import leo13.LeoObject
import leo13.script.plus

data class ExprLink(val lhs: Expr, val op: Op) : LeoObject() {
	override fun toString() = super.toString()
	override val scriptableName get() = "link"
	override val scriptableBody get() = lhs.scriptableBody.plus(op.scriptableLine)
}

fun link(lhs: Expr, operator: Op) = ExprLink(lhs, operator)
