package leo13.js

data class Apply(val lhs: Expression, val rhs: Expression)

infix fun Expression.apply(rhs: Expression) = Apply(this, rhs)

val Apply.code get() = "${lhs.code}(${rhs.code})"