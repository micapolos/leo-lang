package leo13.script

data class Case(val name: String, val expr: Expr)
infix fun String.caseTo(expr: Expr) = Case(this, expr)
