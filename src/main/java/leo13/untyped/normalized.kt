package leo13.untyped

import leo13.LeoStruct
import leo13.script.Script

data class Normalized(val script: Script) : LeoStruct("normalized", script) {
	override fun toString() = super.toString()
}

fun normalized(script: Script) = Normalized(script)