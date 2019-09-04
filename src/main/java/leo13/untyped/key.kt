package leo13.untyped

import leo13.LeoStruct
import leo13.script.Script

data class key(val script: Script) : LeoStruct("key", script) {
	override fun toString() = super.toString()
}