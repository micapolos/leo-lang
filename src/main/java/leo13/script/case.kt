package leo13.script

import leo13.AsScriptLine
import leo13.lineTo
import leo13.script

data class Case(val name: String, val expr: Expr) : AsScriptLine() {
	override fun toString() = asScriptLine.toString()
	override val asScriptLine
		get() = "case" lineTo script(name lineTo script(expr.asScriptLine))
}

infix fun String.caseTo(expr: Expr) = Case(this, expr)
