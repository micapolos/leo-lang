package leo13.script.evaluator

import leo13.Script
import leo13.asScript
import leo13.lineTo
import leo9.Stack
import leo9.push
import leo9.stack

data class Bindings(val stack: Stack<Script>) {
	override fun toString() = asScriptLine.toString()
	val asScriptLine
		get() = "bindings" lineTo stack.asScript { asScriptLine }
}

val Stack<Script>.bindings get() = Bindings(this)
fun Bindings.push(script: Script) = stack.push(script).bindings
fun bindings(vararg scripts: Script) = stack(*scripts).bindings
