package leo13

data class Function(val parameter: TypeParameter, val typedExpr: TypedExpr)

fun function(parameter: TypeParameter, typedExpr: TypedExpr) = Function(parameter, typedExpr)
