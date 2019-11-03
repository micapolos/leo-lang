package leo14

sealed class Token

data class NumberToken(val number: Number) : Token()
data class StringToken(val string: String) : Token()
data class BeginToken(val begin: Begin) : Token()
data class EndToken(val end: End) : Token()

fun token(string: String): Token = StringToken(string)
fun token(number: Number): Token = NumberToken(number)
fun token(int: Int): Token = token(number(int))
fun token(double: Double): Token = token(number(double))
fun token(begin: Begin): Token = BeginToken(begin)
fun token(end: End): Token = EndToken(end)
