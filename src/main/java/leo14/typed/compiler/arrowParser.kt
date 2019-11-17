package leo14.typed.compiler

import leo14.BeginToken
import leo14.EndToken
import leo14.LiteralToken
import leo14.Token
import leo14.typed.Arrow
import leo14.typed.line

data class ArrowParser<T>(
	val ender: ArrowEnder<T>,
	val arrow: Arrow)

data class ArrowEnder<T>(
	val typeParser: TypeParser<T>)

fun <T> ArrowParser<T>.parse(token: Token): Leo<T> =
	when (token) {
		is LiteralToken -> null
		is BeginToken -> null
		is EndToken -> ender.end(arrow)
	} ?: error("$this.parse($token)")

fun <T> ArrowEnder<T>.end(arrow: Arrow): Leo<T> =
	leo(typeParser.plus(line(arrow)))
