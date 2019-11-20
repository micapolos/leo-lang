package leo14.syntax

import leo14.Token
import leo14.begin
import leo14.end
import leo14.token

val SyntaxToken.token: Token
	get() =
		when (this) {
			is LiteralSyntaxToken -> token(literal)
			is BeginSyntaxToken -> token(begin(name.string))
			is EndSyntaxToken -> token(end)
		}
