package leo13.token

import leo13.LeoObject
import leo13.Scripting
import leo13.script.Script
import leo13.script.lineTo
import leo13.script.script

sealed class Token : LeoObject(), Scripting {
	override fun toString() = scriptableLine.toString()
	override val scriptableName get() = "token"
	override val scriptableBody get() = script(tokenScriptableLine)
	abstract val tokenScriptableName: String
	abstract val tokenScriptableBody: Script
	val tokenScriptableLine get() = tokenScriptableName lineTo tokenScriptableBody
	override val scriptingLine get() = scriptableLine
}

data class OpeningToken(val opening: Opening) : Token() {
	override fun toString() = super.toString()
	override val tokenScriptableName get() = opening.asScriptLine.name
	override val tokenScriptableBody get() = opening.asScriptLine.rhs
}

data class ClosingToken(val closing: Closing) : Token() {
	override fun toString() = super.toString()
	override val tokenScriptableName get() = closing.asScriptLine.name
	override val tokenScriptableBody get() = closing.asScriptLine.rhs
}

fun token(opening: Opening): Token = OpeningToken(opening)
fun token(end: Closing): Token = ClosingToken(end)

fun Token.canAppend(token: Token): Boolean =
	this is ClosingToken || token is ClosingToken

fun Token?.orNullCanAppend(token: Token): Boolean =
	if (this == null) token is OpeningToken
	else canAppend(token)
