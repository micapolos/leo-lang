package leo13.script

data class Writer<in V>(val name: String, val bodyScriptFn: V.() -> Script)

fun <V> writer(name: String, scriptFn: V.() -> Script) = Writer(name, scriptFn)
fun <V> Writer<V>.bodyScript(value: V): Script = value.bodyScriptFn()
fun <V> Writer<V>.scriptLine(value: V): ScriptLine = name lineTo bodyScript(value)
fun <V> Writer<V>.script(value: V): Script = script(scriptLine(value))
fun <V> Writer<V>.string(value: V): String = scriptLine(value).toString()

fun <V> writer(name: String): Writer<V> = writer(name) { script() }
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
