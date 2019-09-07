package leo13.untyped

import leo13.LeoStruct
import leo13.script.*

val evaluatedName = "evaluated"
val evaluatedReader: Reader<Evaluated> = reader(evaluatedName, scriptReader, ::evaluated)
val evaluatedWriter: Writer<Evaluated> = writer(evaluatedName, field(scriptWriter) { script })

data class Evaluated(val script: Script) : LeoStruct("evaluated", script) {
	override fun toString() = super.toString()
}

fun evaluated(script: Script) = Evaluated(script)
