package leo14.typed.compiler

import leo14.EndToken
import leo14.Token
import leo14.typed.Action
import leo14.typed.plus

data class ActionParser<T>(
	val parentCompiledParser: CompiledParser<T>,
	val action: Action<T>)

fun <T> ActionParser<T>.parse(token: Token): Compiler<T> =
	if (token is EndToken)
		parentCompiledParser.next {
			updateTyped {
				plus(action)
			}
		}
	else error("$this.parse($token")