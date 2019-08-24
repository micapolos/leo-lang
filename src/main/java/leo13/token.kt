package leo13

sealed class Token {
	override fun toString() = asScript.toString()
	abstract val asScript: Script
}

data class OpeningToken(val opening: Opening) : Token() {
	override fun toString() = super.toString()
	override val asScript = script("opening" lineTo opening.asScript)
}

data class ClosingToken(val end: Closing) : Token() {
	override fun toString() = super.toString()
	override val asScript = script("end" lineTo script())
}

fun token(opening: Opening): Token = OpeningToken(opening)
fun token(end: Closing): Token = ClosingToken(end)
