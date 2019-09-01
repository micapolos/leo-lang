package leo13.value

import leo13.Scriptable
import leo13.script.script

data class Call(val expr: Expr) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = "call"
	override val scriptableBody get() = script(expr.scriptableLine)
}

fun call(expr: Expr) = Call(expr)
