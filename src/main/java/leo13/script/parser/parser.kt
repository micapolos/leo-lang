package leo13.script.parser

import leo.base.notNullIf
import leo13.*

data class Parser(
	val scriptHead: ScriptHead,
	val errorOrNull: TokenError?) {
	override fun toString() = asScript.toString()
	val asScript
		get() = script(
			"head" lineTo nullScript,
			"error" lineTo errorOrNull.orNullAsScript { asScript })
}

data class TokenError(val token: Token) {
	override fun toString() = asScript.toString()
	val asScript get() = script("token" lineTo token.asScript)
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