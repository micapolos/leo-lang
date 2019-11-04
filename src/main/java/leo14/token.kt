package leo14

sealed class Token

data class LiteralToken(val literal: Literal) : Token()
data class BeginToken(val begin: Begin) : Token()
data class EndToken(val end: End) : Token()

fun token(literal: Literal): Token = LiteralToken(literal)
fun token(string: String): Token = token(literal(string))
fun token(number: Number): Token = token(literal(number))
fun token(int: Int): Token = token(number(int))
fun token(double: Double): Token = token(number(double))
fun token(begin: Begin): Token = BeginToken(begin)
fun token(end: End): Token = EndToken(end)
