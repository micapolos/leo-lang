package leo13.js

sealed class Token

data class DoubleToken(val double: Double) : Token()
data class StringToken(val string: String) : Token()
data class BeginToken(val begin: Begin) : Token()
data class EndToken(val end: End) : Token()

fun token(string: String): Token = StringToken(string)
fun token(double: Double): Token = DoubleToken(double)
fun token(begin: Begin): Token = BeginToken(begin)
fun token(end: End): Token = EndToken(end)
