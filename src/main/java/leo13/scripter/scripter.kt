package leo13.scripter

import leo13.script.*

data class Scripter<V : Any>(
	val name: String,
	val bodyScript: V.() -> Script,
	val unsafeBodyValue: Script.() -> V)

fun <V : Any> Scripter<V>.scriptLine(value: V): ScriptLine =
	name lineTo bodyScript(value)

fun <V : Any> Scripter<V>.script(value: V): Script =
	script(scriptLine(value))

fun <V : Any> Scripter<V>.unsafeValue(scriptLine: ScriptLine): V =
	unsafeBodyValue(scriptLine.unsafeRhs(name))

fun <V : Any> Scripter<V>.unsafeValue(script: Script): V =
	unsafeValue(script.unsafeOnlyLine)

fun <V : Any> Scripter<V>.toString(value: V): String =
	scriptLine(value).toString()