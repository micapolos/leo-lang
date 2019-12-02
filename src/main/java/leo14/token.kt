package leo14

sealed class Token

data class LiteralToken(val literal: Literal) : Token() {
	override fun toString() = "$literal"
}

data class BeginToken(val begin: Begin) : Token() {
	override fun toString() = "$begin"
}

data class EndToken(val end: End) : Token() {
	override fun toString() = "$end"
}

fun token(literal: Literal): Token = LiteralToken(literal)
fun token(string: String): Token = token(literal(string))
fun token(number: Number): Token = token(literal(number))
fun token(int: Int): Token = token(number(int))
fun token(double: Double): Token = token(number(double))
fun token(begin: Begin): Token = BeginToken(begin)
fun token(end: End): Token = EndToken(end)

val Token.reflectScriptLine
	get() =
		"token" lineTo script(
			when (this) {
				is LiteralToken -> literal.reflectScriptLine
				is BeginToken -> begin.reflectScriptLine
				is EndToken -> end.reflectScriptLine
			})