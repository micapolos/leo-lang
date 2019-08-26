package leo13.token

import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script

sealed class Token {
	override fun toString() = asScriptLine.toString()
	val asScriptLine get() = "token" lineTo script(tokenAsScriptLine)
	abstract val tokenAsScriptLine: ScriptLine
}

data class OpeningToken(val opening: Opening) : Token() {
	override fun toString() = super.toString()
	override val tokenAsScriptLine = opening.asScriptLine
}

data class ClosingToken(val closing: Closing) : Token() {
	override fun toString() = super.toString()
	override val tokenAsScriptLine = Closing.asScriptLine
}

fun token(opening: Opening): Token = OpeningToken(opening)
fun token(end: Closing): Token = ClosingToken(end)

fun Token.canAppend(token: Token): Boolean =
	this is ClosingToken || token is ClosingToken

fun Token?.orNullCanAppend(token: Token): Boolean =
	if (this == null) token is OpeningToken
	else canAppend(token)
