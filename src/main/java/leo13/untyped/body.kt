package leo13.untyped

import leo13.LeoStruct
import leo13.script.Script
import leo13.script.script

data class body(val script: Script = script()) : LeoStruct("body", script) {
	override fun toString() = super.toString()
}