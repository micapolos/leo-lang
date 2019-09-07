package leo13.untyped

import leo.base.notNullIf
import leo13.LeoStruct
import leo13.script.*

const val bindingName = "binding"

val bindingReader: Reader<Binding> =
	reader(bindingName, keyReader, valueReader, ::binding)

val bindingWriter: Writer<Binding> =
	writer(
		bindingName,
		field(keyWriter) { key },
		field(valueWriter) { value })

data class Binding(val key: Key, val value: Value) : LeoStruct("binding", key, value) {
	override fun toString() = super.toString()
}

fun binding(key: Key, value: Value) = Binding(key, value)

fun Binding.valueOrNull(script: Script): Value? =
	notNullIf(key.script == script) { value }