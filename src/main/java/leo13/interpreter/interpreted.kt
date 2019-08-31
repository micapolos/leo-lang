package leo13.interpreter

import leo13.compiler.Trace
import leo13.compiler.trace
import leo13.script.Scriptable
import leo13.script.script
import leo13.value.Value
import leo13.value.value

data class Interpreted(val value: Value, val trace: Trace) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = "interpreted"
	override val scriptableBody get() = script(value.scriptableLine, trace.scriptableLine)
}

fun interpreted(value: Value, trace: Trace) = Interpreted(value, trace)
fun interpreted() = interpreted(value(), trace())
