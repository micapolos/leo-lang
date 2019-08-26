package leo13.script

import leo13.lineTo
import leo13.script
import leo13.value.Expr

data class Call(val expr: Expr) {
	override fun toString() = asScriptLine.toString()
	val asScriptLine get() = "call" lineTo script(expr.scriptableLine)
}

fun call(expr: Expr) = Call(expr)
