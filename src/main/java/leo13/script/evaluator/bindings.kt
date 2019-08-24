package leo13.script.evaluator

import leo13.Script
import leo13.lineTo
import leo13.script
import leo9.*

data class Bindings(val stack: Stack<Script>) {
	override fun toString() = asScript.toString()
	val asScript
		get() =
			if (stack.isEmpty) script("null" lineTo script())
			else stack.map { "binding" lineTo this }.script
}

val Stack<Script>.bindings get() = Bindings(this)
fun Bindings.push(script: Script) = stack.push(script).bindings
fun bindings(vararg scripts: Script) = stack(*scripts).bindings
