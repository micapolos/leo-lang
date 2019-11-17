package leo14.typed.compiler

import leo14.BeginToken
import leo14.EndToken
import leo14.LiteralToken
import leo14.Token
import leo14.typed.Arrow
import leo14.typed.line

data class ArrowParser(
	val ender: ArrowEnder,
	val arrow: Arrow)

data class ArrowEnder(val typeParser: TypeParser)

fun <T> ArrowParser.parse(token: Token): Leo<T> =
	when (token) {
		is LiteralToken -> null
		is BeginToken -> null
		is EndToken -> ender.end(arrow)
	} ?: error("$this.parse($token)")

fun <T> ArrowEnder.end(arrow: Arrow): Leo<T> =
	leo(typeParser.plus<T>(line(arrow)))
