package leo13.js2

data class Lambda(val name: String, val expr: Expr)

fun lambda(name: String, expr: Expr) = Lambda(name, expr)
infix fun String.ret(expr: Expr) = expr(lambda(this, expr))
val Lambda.exprCode get() = "$name=>${expr.code}"
