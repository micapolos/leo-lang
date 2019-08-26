package leo13.script

import leo.base.notNullIf
import leo.base.orNullFold
import leo13.token.Token
import leo13.token.Tokens
import leo9.reverse
import leo9.seq

data class Parser(
	val scriptHead: ScriptHead,
	val errorOrNull: TokenError?) {
	override fun toString() = asScriptLine.toString()
	val asScriptLine
		get() = "parser" lineTo script(
			scriptHead.asScriptLine,
			errorOrNull.orNullAsScriptLine("error"))
}

data class TokenError(val token: Token) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = "error"
	override val scriptableBody get() = script(token.asScriptLine)
}

fun error(token: Token) = TokenError(token)

val ScriptHead.parser: Parser get() = Parser(this, null)
val TokenError.parser: Parser get() = Parser(scriptHead(), this)
fun parser(): Parser = scriptHead().parser

fun Parser.push(token: Token): Parser =
	if (errorOrNull != null) this
	else scriptHead.plus(token)?.parser ?: put(error(token))

val Parser.okScriptHeadOrNull get() = notNullIf(errorOrNull == null) { scriptHead }
val Parser.completedScriptOrNull get() = okScriptHeadOrNull?.completeScriptOrNull

fun Parser.put(error: TokenError) = copy(errorOrNull = error)

val Tokens.parse: Script?
	get() =
		parser().orNullFold(stack.reverse.seq) { push(it) }?.completedScriptOrNull


