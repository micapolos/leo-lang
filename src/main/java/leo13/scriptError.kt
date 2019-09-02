package leo13

import leo13.script.Script

data class ScriptError(val script: Script) : LeoObject() {
	override fun toString() = super.toString()
	override val scriptableName get() = "error"
	override val scriptableBody get() = script
}

fun error(script: Script) = ScriptError(script)
