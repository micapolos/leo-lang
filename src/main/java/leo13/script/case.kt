package leo13.script

import leo13.Scriptable
import leo13.lineTo
import leo13.script

data class Case(val name: String, val expr: Expr) : Scriptable() {
	override fun toString() = asScriptLine.toString()
	override val asScriptLine
		get() = "case" lineTo script(name lineTo script(), "to" lineTo script(expr.asScriptLine))
}

infix fun String.caseTo(expr: Expr) = Case(this, expr)
