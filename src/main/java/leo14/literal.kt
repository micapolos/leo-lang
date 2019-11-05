package leo14

sealed class Literal

data class StringLiteral(val string: String) : Literal() {
	override fun toString() = "\"$string\""
}

data class NumberLiteral(val number: Number) : Literal() {
	override fun toString() = "$number"
}

fun literal(string: String): Literal = StringLiteral(string)
fun literal(number: Number): Literal = NumberLiteral(number)

val Literal.any
	get() =
		when (this) {
			is StringLiteral -> string
			is NumberLiteral -> number.any
		}