package leo14

abstract class Scriptable {
	abstract val reflectScriptLine: ScriptLine
	override fun toString() = reflectScriptLine.toString()
}

val Any?.anyReflectScriptLine: ScriptLine
	get() =
		if (this is Scriptable) reflectScriptLine
		else "any" lineTo script(literal(toString()))