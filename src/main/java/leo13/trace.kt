package leo13

import leo13.script.Script
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script

data class TracedError(val stack: Stack<Trace> = stack()) : Exception() {
	override fun toString() = scriptLine.toString()
}

data class Trace(val scriptLineFn: () -> ScriptLine) {
	override fun toString() = scriptLine.toString()
}

data class Traced<out V>(val fn: () -> V)

fun <V> traced(fn: () -> V) = Traced(fn)

val Trace.scriptLine get() = scriptLineFn()

fun trace(scriptLineFn: () -> ScriptLine) = Trace(scriptLineFn)

fun <V> Trace.traced(fn: () -> V): V =
	try {
		fn()
	} catch (tracedError: TracedError) {
		throw TracedError(tracedError.stack.push(this)).initCause(tracedError)
	}

fun <V> tracedError(): V =
	throw TracedError()

fun <V> tracedError(line: ScriptLine): V =
	trace { line }.traced { tracedError() }

val TracedError.scriptLine
	get() =
		errorName lineTo stack.reverse.map { scriptLine }.script

fun <V> Traced<V>.onError(errorFn: Script.() -> V): V =
	try {
		fn()
	} catch (tracedError: TracedError) {
		tracedError.scriptLine.rhs.errorFn()
	}

val <V : Any> V?.orTracedError: V
	get() =
		this ?: tracedError()