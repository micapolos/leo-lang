package leo13.value

import leo13.script.Scriptable
import leo13.script.asScript
import leo9.Stack
import leo9.push
import leo9.stack

data class ValueBindings(val stack: Stack<Value>) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = "bindings"
	override val scriptableBody get() = stack.asScript
}

val Stack<Value>.valueBindings get() = ValueBindings(this)
fun ValueBindings.push(value: Value) = stack.push(value).valueBindings
fun valueBindings(vararg values: Value) = stack(*values).valueBindings

fun ValueBindings.evaluate(expr: Expr): Value =
	evaluator(this, value()).evaluate(expr)
