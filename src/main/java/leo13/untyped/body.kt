package leo13.untyped

import leo13.LeoStruct
import leo13.script.Script

data class Body(val context: Context, val script: Script) : LeoStruct("body", context, script) {
	override fun toString() = super.toString()
}

fun body(context: Context, script: Script) = Body(context, script)