package leo13.script

import leo13.Scriptable
import leo13.lineTo
import leo13.script
import leo13.value.Expr

data class Case(val name: String, val expr: Expr) : Scriptable() {
	override fun toString() = scriptableLine.toString()
	override val scriptableName get() = "case"
	override val scriptableBody get() = script(name lineTo script(expr.scriptableLine))
}

infix fun String.caseTo(expr: Expr) = Case(this, expr)
