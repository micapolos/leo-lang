package leo14.js.ast

data class Ret(val expr: Expr)

fun ret(expr: Expr) = Ret(expr)
val Ret.stmtCode get() = "return ${expr.code}"