package leo14.typed.compiler

import leo14.BeginToken
import leo14.EndToken
import leo14.LiteralToken
import leo14.Token

data class ForgetEverythingEndParser<T>(val parentCompiledParser: CompiledParser<T>)

fun <T> ForgetEverythingEndParser<T>.parse(token: Token): Compiler<T> =
	when (token) {
		is LiteralToken -> null
		is BeginToken -> null
		is EndToken -> compiler(parentCompiledParser.forgetEverything)
	} ?: error("$this.parse($token)")