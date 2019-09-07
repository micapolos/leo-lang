package leo13.base.type

import leo13.script.*

data class Type<V : Any>(
	val name: String,
	val bodyScript: V.() -> Script,
	val unsafeBodyValue: Script.() -> V)

fun <V : Any> Type<V>.scriptLine(value: V): ScriptLine =
	name lineTo bodyScript(value)

fun <V : Any> Type<V>.script(value: V): Script =
	script(scriptLine(value))

fun <V : Any> Type<V>.unsafeValue(scriptLine: ScriptLine): V =
	unsafeBodyValue(scriptLine.unsafeRhs(name))

fun <V : Any> Type<V>.unsafeValue(script: Script): V =
	unsafeValue(script.unsafeOnlyLine)

fun <V : Any> Type<V>.toString(value: V): String =
	scriptLine(value).toString()