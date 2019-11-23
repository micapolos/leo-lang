package leo14.js.ast

data class Op(val lhs: Expr, val name: String, val rhs: Expr)

fun Expr.op(name: String, rhs: Expr) = Op(this, name, rhs)
val Op.exprCode get() = "(${lhs.code})$name(${rhs.code})"
