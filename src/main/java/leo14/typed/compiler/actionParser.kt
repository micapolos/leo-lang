package leo14.typed.compiler

import leo14.EndToken
import leo14.Token
import leo14.typed.Action
import leo14.typed.plus

data class ActionParser<T>(
	val compiledParser: CompiledParser<T>,
	val action: Action<T>)

fun <T> ActionParser<T>.parse(token: Token): Leo<T> =
	if (token is EndToken)
		leo(
			compiledParser.updateCompiled {
				updateTyped {
					plus(action)
				}
			}
		)
	else error("$this.parse($token")