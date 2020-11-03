package leo14.lambda.java

import leo14.*
import leo14.Number
import leo14.lambda.code.Code
import leo14.lambda.code.code

data class Native(val code: Code)

fun native(code: Code) = Native(code)
val nullNative = native("null")

fun native(int: Int) = native(code("$int"))
fun native(double: Double) = native(code("$double"))
fun native(string: String) = native(code("\"$string\""))
fun native(number: Number) = native(code(number.code))
fun native(literal: Literal) = when (literal) {
	is NumberLiteral -> native(literal.number)
	is StringLiteral -> native(literal.string)
}