package leo13.untyped

import leo13.LeoStruct
import leo13.script.*

const val valueName = "value"
val valueReader: Reader<Value> = reader(valueName, scriptReader, ::value)
val valueWriter: Writer<Value> = writer(valueName, field(scriptWriter) { script })

data class Value(val script: Script) : LeoStruct("value", script) {
	override fun toString() = super.toString()
}

fun value(script: Script) = Value(script)
