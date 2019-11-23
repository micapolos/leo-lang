package leo14.js.ast

data class SetNamed(val lhs: Expr, val name: String, val rhs: Expr)

fun Expr.set(name: String, rhs: Expr) = stmt(SetNamed(this, name, rhs))

val SetNamed.stmtCode get() = "${lhs.code}.$name=${rhs.code}"