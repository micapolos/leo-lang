package leo14.typed.compiler

import leo.base.ifOrNull
import leo14.EndToken
import leo14.Token
import leo14.leonardoScript

data class LeonardoParser<T>(
	val parentCompiledParser: CompiledParser<T>)

fun <T> LeonardoParser<T>.parse(token: Token): Compiler<T> =
	ifOrNull(token is EndToken) {
		parentCompiledParser.plus(leonardoScript).let(::compiler)
	} ?: error("$this.parse($token)")