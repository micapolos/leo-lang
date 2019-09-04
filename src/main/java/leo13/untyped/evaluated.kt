package leo13.untyped

import leo13.LeoStruct
import leo13.script.Script

data class evaluated(val script: Script) : LeoStruct("evaluated", script) {
	override fun toString() = super.toString()
}