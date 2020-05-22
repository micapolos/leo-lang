package leo16.token

sealed class Token

data class WordToken(val word: String) : Token()
object EndToken : Token()

val String.token: Token get() = WordToken(this)
val endToken: Token get() = EndToken

