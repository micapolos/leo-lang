package leo13.js2

data class Set(val lhs: Expr, val rhs: Expr)

fun Expr.set(rhs: Expr) = expr(Set(this, rhs))
val Set.code get() = "${lhs.code} = ${rhs.code}"