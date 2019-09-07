package leo13.untyped

import leo13.LeoStruct
import leo13.script.*

const val keyName = "key"
val keyReader: Reader<Key> = reader(keyName, scriptReader, ::key)
val keyWriter: Writer<Key> = writer(keyName, field(scriptWriter) { script })

data class Key(val script: Script) : LeoStruct("key", script) {
	override fun toString() = super.toString()
}

fun key(script: Script) = Key(script)