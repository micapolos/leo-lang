package leo13.js2

data class Ret(val expr: Expr)

fun ret(expr: Expr) = Ret(expr)
val Ret.stmtCode get() = "return ${expr.code}"