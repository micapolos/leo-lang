package leo13

abstract class Scriptable {
	override fun toString() = asScriptLine.toString()
	abstract val asScriptLine: ScriptLine
}