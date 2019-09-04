package leo13.untyped

import leo13.LeoStruct
import leo13.script.Script

data class normalized(val script: Script) : LeoStruct("normalized", script) {
	override fun toString() = super.toString()
}
