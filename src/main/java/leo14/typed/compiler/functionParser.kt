package leo14.typed.compiler

import leo14.EndToken
import leo14.Token
import leo14.typed.Function
import leo14.typed.plus

data class FunctionParser<T>(
	val parentCompiledParser: CompiledParser<T>,
	val function: Function<T>)

fun <T> FunctionParser<T>.parse(token: Token): Compiler<T> =
	if (token is EndToken)
		parentCompiledParser.nextCompiler {
			updateTyped {
				plus(function)
			}
		}
	else error("$this.parse($token")