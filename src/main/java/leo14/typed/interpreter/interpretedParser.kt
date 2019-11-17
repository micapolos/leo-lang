package leo14.typed.interpreter

import leo14.BeginToken
import leo14.EndToken
import leo14.LiteralToken
import leo14.Token
import leo14.typed.compiler.Dictionary
import leo14.typed.compiler.Leo

data class InterpretedParser<T>(
	val ender: InterpretedEnder<T>?,
	val dictionary: Dictionary,
	val interpreted: Interpreted<T>)

sealed class InterpretedEnder<T>

fun <T> InterpretedParser<T>.parse(token: Token): Leo<T> =
	when (token) {
		is LiteralToken -> TODO()
		is BeginToken -> TODO()
		is EndToken -> ender?.end(interpreted)
	} ?: error("$this.parse($token)")

fun <T> InterpretedEnder<T>.end(interpreted: Interpreted<T>): Leo<T> =
	TODO()