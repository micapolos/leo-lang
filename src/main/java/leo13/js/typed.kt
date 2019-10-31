package leo13.js

data class Typed(val expression: Expression, val type: Type)

infix fun Expression.of(type: Type) = Typed(this, type)
val nullTyped = nullExpression of nullType

fun typed(double: Double) = expression(double) of doubleType
fun typed(string: String) = expression(string) of stringType
