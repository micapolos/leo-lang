package leo14.typed.compiler

import leo14.EndToken
import leo14.Token

data class NothingParser<T>(val parentCompiledParser: CompiledParser<T>)

fun <T> NothingParser<T>.parse(token: Token): Leo<T> =
	if (token is EndToken) leo(parentCompiledParser)
	else error("$this.parse($token)")
