package leo13

import leo.base.assertEqualTo
import leo13.script.Script
import leo13.script.ScriptLine
import leo13.script.script

fun assertFailsWith(script: Script, fn: () -> Unit) {
	try {
		fn()
	} catch (scriptException: ScriptException) {
		scriptException.script.assertEqualTo(script)
	}
}

data class Traced<out V>(val fn: () -> V)

fun <V> traced(fn: () -> V) = Traced(fn)

fun <V> Traced<V>.assertFailsWith(vararg lines: ScriptLine) {
	try {
		fn()
		kotlin.test.fail("TracedError expected")
	} catch (tracedError: TracedError) {
		tracedError.scriptLine.rhs.assertEqualTo(script(*lines))
	}
}

fun <V> Stack<V>.assertContains(vararg values: V) =
	assertEqualTo(stack(*values))