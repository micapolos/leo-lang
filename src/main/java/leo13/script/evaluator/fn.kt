package leo13.script.evaluator

import leo13.AsScriptLine
import leo13.lineTo
import leo13.script
import leo13.script.Expr
import leo13.script.expr

data class Fn(val bindings: Bindings, val expr: Expr) : AsScriptLine() {
	override fun toString() = super.toString()
	override val asScriptLine = "fn" lineTo script(bindings.asScriptLine, expr.asScriptLine)
}

fun fn(bindings: Bindings, expr: Expr) = Fn(bindings, expr)
fun fn() = fn(bindings(), expr())