package leo13.script.evaluator

import leo13.Value
import leo13.asScript
import leo13.lineTo
import leo9.Stack
import leo9.push
import leo9.stack

data class Bindings(val stack: Stack<Value>) {
	override fun toString() = asScriptLine.toString()
	val asScriptLine
		get() = "bindings" lineTo stack.asScript { asScriptLine }
}

val Stack<Value>.bindings get() = Bindings(this)
fun Bindings.push(value: Value) = stack.push(value).bindings
fun bindings(vararg values: Value) = stack(*values).bindings
