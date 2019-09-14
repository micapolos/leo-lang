package leo13.token.reader

import leo13.Colon
import leo13.LeoObject
import leo13.script.Script
import leo13.script.lineTo
import leo13.script.script

sealed class Head : LeoObject() {
	override fun toString() = scriptableLine.toString()
	override val scriptableName get() = "head"
	override val scriptableBody get() = script(headScriptableLine)
	abstract val headScriptableName: String
	abstract val headScriptableBody: Script
	val headScriptableLine get() = headScriptableName lineTo headScriptableBody
}

data class InputHead(val input: Input) : Head() {
	override fun toString() = super.toString()
	override val headScriptableName get() = input.scriptableName
	override val headScriptableBody get() = input.scriptableBody
}

data class ColonHead(val colon: Colon) : Head() {
	override fun toString() = super.toString()
	override val headScriptableName get() = "colon"
	override val headScriptableBody get() = script()
}

data class IndentHead(val indent: Indent) : Head() {
	override fun toString() = super.toString()
	override val headScriptableName get() = indent.scriptableName
	override val headScriptableBody get() = indent.scriptableBody
}

fun head(input: Input): Head = InputHead(input)
fun head(colon: Colon): Head = ColonHead(colon)
fun head(indent: Indent): Head = IndentHead(indent)

fun head() = head(input(colon(false), ""))