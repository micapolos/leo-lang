package leo13.untyped

import leo13.LeoStruct
import leo13.script.Script

data class value(val script: Script) : LeoStruct("value", script) {
	override fun toString() = super.toString()
}
