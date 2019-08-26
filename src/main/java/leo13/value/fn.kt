package leo13.value

import leo13.Scriptable
import leo13.script

data class Fn(val valueBindings: ValueBindings, val expr: Expr) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = "fn"
	override val scriptableBody get() = script(valueBindings.scriptableLine, expr.scriptableLine)
}

fun fn(valueBindings: ValueBindings, expr: Expr) = Fn(valueBindings, expr)
fun fn() = fn(valueBindings(), expr())