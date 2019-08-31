package leo13

import leo13.script.Script
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script

data class ScriptException(val script: Script) : Exception() {
	override fun toString() = script.toString()
}

fun <V> fail(script: Script): V = throw ScriptException(script)
fun <V> fail(scriptLine: ScriptLine): V = fail(script(scriptLine))
fun <V> fail(name: String): V = fail(script(name))

fun <V> failRun(name: String, fn: () -> V): V =
	try {
		fn()
	} catch (scriptException: ScriptException) {
		throw fail(name lineTo scriptException.script)
	}
