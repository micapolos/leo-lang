package leo13.untyped

import leo13.LeoStruct
import leo13.script.Script

data class Evaluated(val script: Script) : LeoStruct("evaluated", script) {
	override fun toString() = super.toString()
}

fun evaluated(script: Script) = Evaluated(script)
