package leo13

import leo13.script.Expr
import leo13.script.evaluator.Bindings
import leo13.script.evaluator.bindings
import leo13.script.expr

data class Fn(val bindings: Bindings, val expr: Expr) : AsScriptLine() {
	override fun toString() = super.toString()
	override val asScriptLine = "fn" lineTo script(bindings.asScriptLine, expr.asScriptLine)
}

fun fn(bindings: Bindings, expr: Expr) = Fn(bindings, expr)
fun fn() = fn(bindings(), expr())