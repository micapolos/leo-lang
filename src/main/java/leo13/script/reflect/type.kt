package leo13.script.reflect

import leo13.fail
import leo13.script.Script
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script

data class Type<V : Any>(val name: String, val body: Body<V>)

fun <V : Any> type(name: String, body: Body<V>) = Type(name, body)

fun <V : Any> Type<V>.scriptLine(value: V): ScriptLine =
	name lineTo bodyScript(value)

fun <V : Any> Type<V>.bodyScript(value: V): Script =
	body.script(value)

fun <V : Any> Type<V>.unsafeValue(scriptLine: ScriptLine): V =
	if (name != scriptLine.name) fail("expected" lineTo script(name))
	else unsafeBodyValue(scriptLine.rhs)

fun <V : Any> Type<V>.unsafeBodyValue(script: Script): V =
	body.unsafeValue(script)
