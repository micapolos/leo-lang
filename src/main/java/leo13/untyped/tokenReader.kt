package leo13.untyped

import leo13.token.ClosingToken
import leo13.token.OpeningToken
import leo13.token.Token

interface TokenReader {
	fun begin(name: String): TokenReader?
	val end: TokenReader?

	fun plus(token: Token): TokenReader? =
		when (token) {
			is OpeningToken -> begin(token.opening.name)
			is ClosingToken -> end
		}
}
