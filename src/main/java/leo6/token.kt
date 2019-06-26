package leo6

sealed class Token

data class BeginToken(val begin: Begin) : Token() {
	override fun toString() = "$begin"
}

data class EndToken(val end: End) : Token() {
	override fun toString() = "$end"
}

fun token(begin: Begin): Token = BeginToken(begin)
fun token(end: End): Token = EndToken(end)
