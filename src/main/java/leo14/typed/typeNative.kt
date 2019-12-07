package leo14.typed

import leo14.ScriptLine
import leo14.lineTo
import leo14.script

data class LineNative(
	val name: String,
	val isStatic: Boolean) {
	override fun toString() = "$scriptLine"
}

fun native(name: String, isStatic: Boolean) =
	LineNative(name, isStatic)

val LineNative.scriptLine: ScriptLine
	get() =
		"native" lineTo script(
			"name" lineTo script(name),
			"static" lineTo script("$isStatic"))