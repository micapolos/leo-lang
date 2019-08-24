package leo13

sealed class Token {
	override fun toString() = asScript.toString()
	abstract val asScript: Script
}

data class OpeningToken(val opening: Opening) : Token() {
	override fun toString() = super.toString()
	override val asScript = script("opening" lineTo opening.asScript)
}

data class ClosingToken(val closing: Closing) : Token() {
	override fun toString() = super.toString()
	override val asScript = script("end" lineTo script())
}

fun token(opening: Opening): Token = OpeningToken(opening)
fun token(end: Closing): Token = ClosingToken(end)

fun Token.canAppend(token: Token): Boolean =
	this is ClosingToken || token is ClosingToken

fun Token?.orNullCanAppend(token: Token): Boolean =
	if (this == null) token is OpeningToken
	else canAppend(token)
