package leo13.value

import leo13.LeoObject
import leo13.script.script

data class Fn(val valueBindings: ValueBindings, val expr: Expr) : LeoObject() {
	override fun toString() = super.toString()
	override val scriptableName get() = "fn"
	override val scriptableBody get() = script(valueBindings.scriptableLine, expr.scriptableLine)
}

fun fn(valueBindings: ValueBindings, expr: Expr) = Fn(valueBindings, expr)
fun fn() = fn(valueBindings(), expr())