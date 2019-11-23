package leo14.typed.compiler

import leo14.BeginToken
import leo14.EndToken
import leo14.LiteralToken
import leo14.Token

data class ForgetEverythingParser<T>(val parentCompiledParser: CompiledParser<T>)

fun <T> ForgetEverythingParser<T>.parse(token: Token): Compiler<T> =
	when (token) {
		is LiteralToken -> null
		is BeginToken -> null
		is EndToken -> ForgetEverythingEndParserCompiler(ForgetEverythingEndParser(parentCompiledParser))
	} ?: error("$this.parse($token)")