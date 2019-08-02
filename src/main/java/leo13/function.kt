package leo13

import leo.base.notNullIf

data class Function(val parameter: TypeParameter, val typedExpr: TypedExpr)

fun function(parameter: TypeParameter, typedExpr: TypedExpr) = Function(parameter, typedExpr)

fun Function.typedExprOrNull(parameter: TypeParameter) =
	notNullIf(this.parameter == parameter) {
		typedExpr
	}
