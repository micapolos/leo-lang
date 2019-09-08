package leo13.script

import leo9.Stack
import leo9.map

data class Writer<in V>(val name: String, val bodyScriptFn: V.() -> Script)

fun <V> writer(name: String, scriptFn: V.() -> Script) = Writer(name, scriptFn)
fun <V> Writer<V>.bodyScript(value: V): Script = value.bodyScriptFn()
fun <V> Writer<V>.scriptLine(value: V): ScriptLine = name lineTo bodyScript(value)
fun <V> Writer<V>.script(value: V): Script = script(scriptLine(value))
fun <V> Writer<V>.string(value: V): String = scriptLine(value).toString()

fun <V> writer(name: String, value: V): Writer<V> = writer(name) { script() }
fun <V> writer(name: String, writer: Writer<V>): Writer<V> = writer(name) { script(writer.scriptLine(this)) }

fun <V, V1> writer(name: String, field1: WriterField<V, V1>): Writer<V> =
	writer(name) {
		script(field1.scriptLine(this))
	}

fun <V, V1, V2> writer(name: String, field1: WriterField<V, V1>, field2: WriterField<V, V2>): Writer<V> =
	writer(name) {
		script(
			field1.scriptLine(this),
			field2.scriptLine(this))
	}

fun <V, V1, V2, V3, V4, V5, V6, V7, V8> writer(
	name: String,
	field1: WriterField<V, V1>,
	field2: WriterField<V, V2>,
	field3: WriterField<V, V3>,
	field4: WriterField<V, V4>,
	field5: WriterField<V, V5>,
	field6: WriterField<V, V6>,
	field7: WriterField<V, V7>,
	field8: WriterField<V, V8>): Writer<V> =
	writer(name) {
		script(
			field1.scriptLine(this),
			field2.scriptLine(this),
			field3.scriptLine(this),
			field4.scriptLine(this),
			field5.scriptLine(this),
			field6.scriptLine(this),
			field7.scriptLine(this),
			field8.scriptLine(this))
	}

fun <V, A> stackWriter(name: String, itemWriter: Writer<A>, fn: V.() -> Stack<A>): Writer<V> =
	writer(name) {
		fn().map { itemWriter.scriptLine(this) }.script
	}
