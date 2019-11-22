package leo14.typed.compiler

import leo14.EndToken
import leo14.Token

data class MemoryItemParser<T>(
	val parentCompiledParser: CompiledParser<T>,
	val memoryItem: MemoryItem<T>)

fun <T> MemoryItemParser<T>.parse(token: Token): Leo<T> =
	if (token is EndToken)
		parentCompiledParser.next {
			updateMemory { plus(memoryItem) }
		}
	else
		error("$this.parse($token)")