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