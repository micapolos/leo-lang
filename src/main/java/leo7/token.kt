package leo7

sealed class Token

data class BeginToken(val begin: WordBegin) : Token()
data class EndToken(val end: End) : Token()

val WordBegin.token: Token get() = BeginToken(this)
val End.token: Token get() = EndToken(this)
