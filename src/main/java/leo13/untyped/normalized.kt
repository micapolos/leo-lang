package leo13.untyped

import leo13.LeoStruct
import leo13.script.*

val normalizedName = "normalized"
val normalizedReader: Reader<Normalized> = reader(normalizedName, scriptReader, ::normalized)
val normalizedWriter: Writer<Normalized> = writer(normalizedName, field(scriptWriter) { script })

data class Normalized(val script: Script) : LeoStruct("normalized", script) {
	override fun toString() = super.toString()
}

fun normalized(script: Script) = Normalized(script)