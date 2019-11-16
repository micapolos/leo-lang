package leo14.typed.compiler

import leo.base.notNullIf
import leo14.EndToken
import leo14.Token
import leo14.typed.nativeLine

data class NativeParser(val typeParser: TypeParser)

fun <T> NativeParser.parse(token: Token): Leo<T> =
	notNullIf(token is EndToken) {
		leo<T>(typeParser.plus<T>(nativeLine))
	} ?: error("$this.parse($token)")
