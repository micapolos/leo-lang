package leo13.value

import leo13.script.Scriptable
import leo13.script.lineTo
import leo13.script.script

data class ExprLine(val name: String, val rhs: Expr) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = "line"
	override val scriptableBody get() = script(name lineTo rhs.scriptableBody)
}

infix fun String.lineTo(rhs: Expr) = ExprLine(this, rhs)
