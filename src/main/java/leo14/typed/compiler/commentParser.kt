package leo14.typed.compiler

import leo14.BeginToken
import leo14.EndToken
import leo14.LiteralToken
import leo14.Token

data class CommentParser<T>(val parent: Leo<T>)

fun <T> CommentParser<T>.parse(token: Token): Leo<T> =
	when (token) {
		is LiteralToken -> leo(this)
		is BeginToken -> leo(CommentParser(leo(this)))
		is EndToken -> parent
	}
