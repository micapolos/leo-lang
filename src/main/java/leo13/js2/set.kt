package leo13.js2

data class Set(val lhs: Expr, val rhs: Expr)

infix fun Expr.set(rhs: Expr) = stmt(Set(this, rhs))

val Set.stmtCode get() = "${lhs.code} = ${rhs.code}"