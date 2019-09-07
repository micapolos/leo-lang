package leo13.script.reflect

import leo13.fail
import leo13.script.*

data class Type<V : Any>(val name: String, val body: Body<V>)

fun <V : Any> type(name: String, body: Body<V>) = Type(name, body)

fun <V : Any> Type<V>.scriptLine(value: V): ScriptLine =
	name lineTo bodyScript(value)

fun <V : Any> Type<V>.bodyScript(value: V): Script =
	body.script(value)

fun <V : Any> Type<V>.script(value: V): Script =
	script(scriptLine(value))

fun <V : Any> Type<V>.unsafeValue(scriptLine: ScriptLine): V =
	if (name != scriptLine.name) fail("expected" lineTo script(name))
	else unsafeBodyValue(scriptLine.rhs)

fun <V : Any> Type<V>.unsafeBodyValue(script: Script): V =
	body.unsafeValue(script)

fun <V : Any> Type<V>.unsafeValue(script: Script): V =
	unsafeValue(script.unsafeOnlyLine)

fun <V : Any> Type<V>.toString(value: V): String =
	scriptLine(value).toString()