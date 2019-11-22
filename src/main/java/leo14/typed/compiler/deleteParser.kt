package leo14.typed.compiler

import leo14.EndToken
import leo14.Token

data class DeleteParser<T>(
	val parentCompiledParser: CompiledParser<T>)

fun <T> DeleteParser<T>.parse(token: Token): Compiler<T> =
	if (token is EndToken) parentCompiledParser.delete
	else error("$this.parse($token)")
