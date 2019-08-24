package leo13.script

import leo13.lineTo
import leo13.nameAsScriptLine
import leo13.script

data class Case(val name: String, val expr: Expr) {
	override fun toString() = asScriptLine.toString()
	val asScriptLine
		get() = "case" lineTo script(
			name.nameAsScriptLine,
			expr.asScriptLine)
}

infix fun String.caseTo(expr: Expr) = Case(this, expr)
