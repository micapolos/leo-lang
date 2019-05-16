package leo3

import leo.base.appendableString

sealed class Token

data class BeginToken(val begin: Begin) : Token() {
	override fun toString() = appendableString { it.append(this) }
}

data class EndToken(val end: End) : Token() {
	override fun toString() = appendableString { it.append(this) }
}

fun token(begin: Begin): Token = BeginToken(begin)
fun token(end: End): Token = EndToken(end)

val Token.byteSeq
	get() = when (this) {
		is BeginToken -> begin.byteSeq
		is EndToken -> end.byteSeq
	}

fun Appendable.append(token: Token) =
	when (token) {
		is BeginToken -> append(token.begin)
		is EndToken -> append(token.end)
	}
