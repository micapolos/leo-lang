package leo13.token.reader

import leo13.LeoObject
import leo13.script.Script
import leo13.script.lineTo
import leo13.script.script

sealed class Line : LeoObject() {
	override fun toString() = scriptableLine.toString()
	override val scriptableName get() = "line"
	override val scriptableBody get() = script(lineScriptableLine)
	abstract val lineScriptableName: String
	abstract val lineScriptableBody: Script
	val lineScriptableLine get() = lineScriptableName lineTo lineScriptableBody
}

data class SameLine(val same: Same) : Line() {
	override fun toString() = super.toString()
	override val lineScriptableName get() = same.scriptableName
	override val lineScriptableBody get() = same.scriptableBody
}

data class NewLine(val new: New) : Line() {
	override fun toString() = super.toString()
	override val lineScriptableName get() = new.scriptableName
	override val lineScriptableBody get() = new.scriptableBody
}

fun line(same: Same): Line = SameLine(same)
fun line(new: New): Line = NewLine(new)
