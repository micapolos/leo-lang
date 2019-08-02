package leo13

data class TypedScript(val script: Script, val type: Type)
data class TypedExpr(val expr: Expr, val type: Type)

infix fun Script.of(type: Type) = TypedScript(this, type)
infix fun Expr.of(type: Type) = TypedExpr(this, type)
