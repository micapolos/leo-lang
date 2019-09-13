package leo13

import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script
import leo13.untyped.errorName
import leo9.Stack
import leo9.map
import leo9.push
import leo9.reverse

data class TracedError(val stack: Stack<Trace> = leo9.stack()) : Exception() {
	override fun toString() = scriptLine.toString()
}

data class Trace(val scriptLineFn: () -> ScriptLine) {
	override fun toString() = scriptLine.toString()
}

val Trace.scriptLine get() = scriptLineFn()

fun trace(scriptLineFn: () -> ScriptLine) = Trace(scriptLineFn)

fun <V> Trace.run(fn: () -> V): V =
	try {
		fn()
	} catch (tracedError: TracedError) {
		throw TracedError(tracedError.stack.push(this))
	}

fun <V> tracedError(): V =
	throw TracedError()

val TracedError.scriptLine
	get() =
		errorName lineTo stack.reverse.map { scriptLine }.script

