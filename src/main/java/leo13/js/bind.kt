package leo13.js

data class Bind(val expression: Expression, val inExpression: Expression)

infix fun Expression.bindIn(expression: Expression) = Bind(this, expression)

val Bind.code get() = "bind(${expression.code}, function() { return ${inExpression.code}; })"