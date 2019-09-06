package leo13.untyped

import leo13.LeoStruct
import leo13.script.Script

data class Body(val context: Context, val script: Script, val isMacro: Boolean) : LeoStruct("body", context, script) {
	override fun toString() = super.toString()
}

fun body(context: Context, script: Script) = Body(context, script, false)
fun macroBody(context: Context, script: Script) = Body(context, script, true)