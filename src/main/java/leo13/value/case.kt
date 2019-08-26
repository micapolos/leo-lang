package leo13.value

import leo13.script.Scriptable
import leo13.script.lineTo
import leo13.script.script

data class Case(val name: String, val expr: Expr) : Scriptable() {
	override fun toString() = scriptableLine.toString()
	override val scriptableName get() = "case"
	override val scriptableBody get() = script(name lineTo script(expr.scriptableLine))
}

infix fun String.caseTo(expr: Expr) = Case(this, expr)
