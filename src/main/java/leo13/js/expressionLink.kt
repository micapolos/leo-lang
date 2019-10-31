package leo13.js

data class ExpressionLink(val lhs: Expression, val rhs: Expression)

infix fun Expression.linkTo(rhs: Expression) = ExpressionLink(this, rhs)
val ExpressionLink.code get() = "${lhs.code}; ${rhs.code}"