package leo14.typed.compiler

import leo.base.notNullIf
import leo14.EndToken
import leo14.Token
import leo14.typed.nativeLine

data class NativeParser<T>(val parentTypeParser: TypeParser<T>)

fun <T> NativeParser<T>.parse(token: Token): Leo<T> =
	notNullIf(token is EndToken) {
		leo(parentTypeParser.plus(nativeLine))
	} ?: error("$this.parse($token)")
