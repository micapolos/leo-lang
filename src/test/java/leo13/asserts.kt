package leo13

import leo.base.assertEqualTo
import leo13.script.Script

fun assertFailsWith(script: Script, fn: () -> Unit) {
	try {
		fn()
	} catch (scriptException: ScriptException) {
		scriptException.script.assertEqualTo(script)
	}
}

val Failable<*>.assertFails
	get() =
		assert(this is FailureFailable) { "Expected failure, got: $this" }

fun <V> Failable<V>.assertSucceedsWith(expected: V) =
	when (this) {
		is SuccessFailable -> value.assertEqualTo(expected)
		is FailureFailable -> fail("Failed: $sentence")
	}

