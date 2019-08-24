package leo13.script

import leo13.lineTo
import leo13.script

data class Call(val expr: Expr) {
	override fun toString() = asScriptLine.toString()
	val asScriptLine get() = "call" lineTo script(expr.asScriptLine)
}

fun call(expr: Expr) = Call(expr)
