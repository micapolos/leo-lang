package leo14.typed.compiler

import leo.base.notNullIf
import leo14.EndToken
import leo14.Token
import leo14.typed.Arrow
import leo14.typed.line

data class ArrowParser(
	val interceptor: ArrowInterceptor?,
	val arrow: Arrow)

data class ArrowInterceptor(val typeParser: TypeParser)

fun <T> ArrowParser.parse(token: Token): Leo<T> =
	interceptor
		?.intercept(arrow, token)
		?: error("$this.parse($token)")

fun <T> ArrowInterceptor.intercept(arrow: Arrow, token: Token): Leo<T>? =
	notNullIf(token is EndToken) {
		leo<T>(typeParser.plus<T>(line(arrow)))
	}
