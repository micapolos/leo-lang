package leo13.script

import leo.base.notNullIf
import leo13.Type

data class Function(val parameterType: Type, val typedExpr: TypedExpr)

fun function(parameterType: Type, typedExpr: TypedExpr) = Function(parameterType, typedExpr)

fun Function.typedExprOrNull(parameter: Type) =
	notNullIf(parameterType == parameter) {
		typedExpr
	}
