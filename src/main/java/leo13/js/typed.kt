package leo13.js

data class Typed(val expression: Expression, val types: Types)

infix fun Expression.of(types: Types) = Typed(this, types)
val nullTyped = nullExpression of emptyTypes

fun typed(number: Number) = expression(number) of types(numberType)
fun typed(string: String) = expression(string) of types(stringType)
