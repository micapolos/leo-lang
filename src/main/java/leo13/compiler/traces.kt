package leo13.compiler

import leo.base.notNullIf
import leo13.script.Scriptable
import leo13.script.asScript
import leo9.Stack
import leo9.mapFirst
import leo9.push
import leo9.stack

data class Traces(val traceStack: Stack<Trace>) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = "traces"
	override val scriptableBody get() = traceStack.asScript { scriptableLine }
}

val Stack<Trace>.types get() = Traces(this)
fun traces() = stack<Trace>().types
fun traces(vararg types: Trace) = stack(*types).types
fun Traces.plus(trace: Trace) = traceStack.push(trace).types
fun Traces.resolve(trace: Trace): Trace =
	traceStack.mapFirst {
		notNullIf(contains(trace)) {
			this
		}
	} ?: trace
