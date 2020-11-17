package leo14

import leo.base.runWith
import leo14.untyped.dottedColorsParameter
import leo14.untyped.leoString

abstract class Scriptable {
	abstract val reflectScriptLine: ScriptLine
	override fun toString() = dottedColorsParameter.runWith(false) { reflectScriptLine.leoString }
}

val Any?.anyReflectScriptLine: ScriptLine
	get() =
		if (this is Scriptable) reflectScriptLine
		else "any" lineTo script(literal(toString()))

fun Any?.anyOptionalReflectScriptLine(name: String): ScriptLine =
	if (this == null) name lineTo script("none")
	else anyReflectScriptLine