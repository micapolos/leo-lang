package leo13.js2

data class Ap(val lhs: Expr, val params: Params)

operator fun Expr.invoke(vararg params: Expr) = expr(Ap(this, params(*params)))
val Ap.exprCode get() = "${lhs.code}(${params.code})"