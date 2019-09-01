package leo13.token.reader

import leo13.Scriptable
import leo13.script.Script
import leo13.script.lineTo
import leo13.script.script


sealed class Head : Scriptable() {
	override fun toString() = scriptableLine.toString()
	override val scriptableName get() = "head"
	override val scriptableBody get() = script(headScriptableLine)
	abstract val headScriptableName: String
	abstract val headScriptableBody: Script
	val headScriptableLine get() = headScriptableName lineTo headScriptableBody
}

data class NameHead(val name: String) : Head() {
	override fun toString() = super.toString()
	override val headScriptableName get() = "name"
	override val headScriptableBody get() = script(name)
}

data class ColonHead(val colon: Colon) : Head() {
	override fun toString() = super.toString()
	override val headScriptableName get() = "colon"
	override val headScriptableBody get() = script()
}

data class BlockIndentHead(val tabIndent: TabIndent) : Head() {
	override fun toString() = super.toString()
	override val headScriptableName get() = tabIndent.scriptableName
	override val headScriptableBody get() = tabIndent.scriptableBody
}

fun head(name: String): Head = NameHead(name)
fun head(colon: Colon): Head = ColonHead(colon)
fun head(tabIndent: TabIndent): Head = BlockIndentHead(tabIndent)
