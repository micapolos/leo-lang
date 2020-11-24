package leo21.definition

import leo14.ScriptLine
import leo14.Scriptable
import leo14.lineTo
import leo14.script
import leo21.token.strings.typeKeyword
import leo21.type.Line
import leo21.type.printScriptLine

data class DefinitionType(val line: Line) : Scriptable() {
	override fun toString() = super.toString()
	override val reflectScriptLine: ScriptLine
		get() = "type" lineTo script(line.reflectScriptLine)
}

val DefinitionType.printScriptLine: ScriptLine
	get() =
		"type".typeKeyword lineTo script(line.printScriptLine)
