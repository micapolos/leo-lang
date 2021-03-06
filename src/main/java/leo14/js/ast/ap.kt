package leo14.js.ast

data class Ap(val lhs: Expr, val params: Params)

operator fun Expr.invoke(params: Params) = expr(Ap(this, params))
operator fun Expr.invoke(vararg params: Expr) = expr(Ap(this, params(*params)))
val Ap.exprCode get() = "(${lhs.code})(${params.code})"