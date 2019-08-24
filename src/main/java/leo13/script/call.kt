package leo13.script

data class Call(val expr: Expr)
fun call(expr: Expr) = Call(expr)
