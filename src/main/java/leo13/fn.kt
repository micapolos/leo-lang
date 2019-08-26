package leo13

import leo13.script.Expr
import leo13.script.evaluator.Bindings
import leo13.script.evaluator.bindings
import leo13.script.expr

data class Fn(val bindings: Bindings, val expr: Expr) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = "fn"
	override val scriptableBody get() = script(bindings.asScriptLine, expr.scriptableLine)
}

fun fn(bindings: Bindings, expr: Expr) = Fn(bindings, expr)
fun fn() = fn(bindings(), expr())