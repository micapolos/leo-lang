package leo14

sealed class Literal

data class StringLiteral(val string: String) : Literal()
data class NumberLiteral(val number: Number) : Literal()

fun literal(string: String): Literal = StringLiteral(string)
fun literal(number: Number): Literal = NumberLiteral(number)

val Literal.any
	get() =
		when (this) {
			is StringLiteral -> string
			is NumberLiteral -> number.any
		}