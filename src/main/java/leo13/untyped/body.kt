package leo13.untyped

import leo13.LeoStruct
import leo13.script.Script
import leo13.script.script

data class Body(val script: Script) : LeoStruct("body", script) {
	override fun toString() = super.toString()
}

fun body(script: Script = script()) = Body(script)