package leo13.value

import leo13.script.lineTo
import leo13.script.script

data class Call(val expr: Expr) {
	override fun toString() = asScriptLine.toString()
	val asScriptLine get() = "call" lineTo script(expr.scriptableLine)
}

fun call(expr: Expr) = Call(expr)
