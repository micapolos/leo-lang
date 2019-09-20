package leo13.script

import leo.base.notNullIf
import leo.base.orNullFold
import leo13.LeoObject
import leo13.orNullAsScriptLine
import leo13.reverse
import leo13.seq
import leo13.token.Token
import leo13.token.Tokens

data class ScriptParser(
	val scriptHead: ScriptHead,
	val errorOrNull: TokenError?) {
	override fun toString() = asScriptLine.toString()
	val asScriptLine
		get() = "parser" lineTo script(
			scriptHead.asScriptLine,
			errorOrNull.orNullAsScriptLine("error"))
}

data class TokenError(val token: Token) : LeoObject() {
	override fun toString() = super.toString()
	override val scriptableName get() = "error"
	override val scriptableBody get() = script(token.scriptableLine)
}

fun error(token: Token) = TokenError(token)

val ScriptHead.scriptParser: ScriptParser get() = ScriptParser(this, null)
val TokenError.scriptParser: ScriptParser get() = ScriptParser(scriptHead(), this)
fun parser(): ScriptParser = scriptHead().scriptParser

fun ScriptParser.push(token: Token): ScriptParser =
	if (errorOrNull != null) this
	else scriptHead.plus(token)?.scriptParser ?: put(error(token))

val ScriptParser.okScriptHeadOrNull get() = notNullIf(errorOrNull == null) { scriptHead }
val ScriptParser.completedScriptOrNull get() = okScriptHeadOrNull?.completeScriptOrNull

fun ScriptParser.put(error: TokenError) = copy(errorOrNull = error)

val Tokens.parse: Script?
	get() =
		parser().orNullFold(stack.reverse.seq) { push(it) }?.completedScriptOrNull


