package leo14.typed.compiler

import leo14.EndToken
import leo14.Token

data class DefinitionParser<T>(
	val parentCompiledParser: CompiledParser<T>,
	val definition: Definition<T>)

fun <T> DefinitionParser<T>.parse(token: Token): Compiler<T> =
	if (token is EndToken)
		parentCompiledParser.next {
			updateMemory { plus(item(definition)) }
		}
	else
		error("$this.parse($token)")