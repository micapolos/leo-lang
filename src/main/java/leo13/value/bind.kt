package leo13.value

import leo13.LeoObject
import leo13.script.script

data class Bind(val expr: Expr) : LeoObject() {
	override fun toString() = super.toString()
	override val scriptableName get() = "bind"
	override val scriptableBody get() = script(expr.scriptableLine)
}

fun bind(expr: Expr) = Bind(expr)
