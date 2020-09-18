package leo18

data class Begin(val string: String)

val String.begin get() = Begin(this)

object End

val end = End

sealed class Token
data class BeginToken(val begin: Begin) : Token()
data class EndToken(val end: End) : Token()

val Begin.token: Token get() = BeginToken(this)
val End.token: Token get() = EndToken(this)
