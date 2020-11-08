package leo21.token.type.compiler

import leo14.BeginToken
import leo14.EndToken
import leo14.LiteralToken
import leo14.Token
import leo21.token.processor.TokenProcessor
import leo21.type.Recursive

data class RecursiveCompiler(
	val parentOrNull: RecursiveParent?,
	val recursiveOrNull: Recursive?
)

fun RecursiveCompiler.plus(token: Token): TokenProcessor =
	when (token) {
		is LiteralToken -> null!!
		is BeginToken -> TODO()
		is EndToken -> TODO()
	}