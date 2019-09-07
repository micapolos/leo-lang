package leo13.base.type

import leo13.fail
import leo13.script.Script
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.unsafeOnlyLine
import leo9.Stack

data class Type<out V : Any>(val name: String, val body: Body<V>)

fun <V : Any> type(name: String, body: Body<V>) = Type(name, body)

fun <V : Any> optionType(orNullType: Type<V>): Type<leo13.base.Option<V>> =
	type(orNullType.name, body(option(orNullType)))

fun <V : Any> listType(itemType: Type<V>): Type<Stack<V>> =
	type(itemType.name, body(list(itemType)))

fun <V : Any> aliasType(name: String, type: Type<V>): Type<V> =
	type(name, body(struct(field(type) { this }) { it }))

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