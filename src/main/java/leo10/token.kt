package leo10

sealed class Token

data class BeginToken(val begin: StringBegin) : Token()
data class EndToken(val end: End) : Token()

fun token(begin: StringBegin): Token = BeginToken(begin)
fun token(end: End): Token = EndToken(end)

val Token.key
	get() =
		when (this) {
			is BeginToken -> begin.string.key
			is EndToken -> "".key
		}