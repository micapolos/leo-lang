package leo13.js2

data class At(val lhs: Expr, val rhs: Expr)

operator fun Expr.get(rhs: Expr) = expr(At(this, rhs))
val At.exprCode get() = "(${lhs.code})[${rhs.code}]"
