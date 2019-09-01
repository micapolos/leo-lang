package leo13.value

import leo13.LeoObject
import leo13.script.script

data class Call(val expr: Expr) : LeoObject() {
	override fun toString() = super.toString()
	override val scriptableName get() = "call"
	override val scriptableBody get() = script(expr.scriptableLine)
}

fun call(expr: Expr) = Call(expr)
