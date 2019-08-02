package leo13

import leo9.Stack
import leo9.mapFirst
import leo9.push
import leo9.stack

data class Scope(val functionStack: Stack<Function>)

val Stack<Function>.scope get() = Scope(this)
fun Scope.plus(function: Function) = functionStack.push(function).scope
fun scope(vararg functions: Function) = stack(*functions).scope

fun Scope.typedExprOrNull(parameter: TypeParameter) =
	functionStack.mapFirst { typedExprOrNull(parameter) }

fun Scope.eval(typedScript: TypedScript): TypedScript =
	evalOrNull(typedScript) ?: typedScript

fun Scope.evalOrNull(typedScript: TypedScript): TypedScript? =
	typedExprOrNull(parameter(typedScript.type))
		?.let { typedExpr ->
			typedExpr.expr.eval(typedScript.value).let { evaledValue ->
				typedExpr.type.script(evaledValue) of typedExpr.type
			}
		}

fun Scope.eval(typedValue: TypedValue): TypedValue =
	evalOrNull(typedValue) ?: typedValue

fun Scope.evalOrNull(typedValue: TypedValue): TypedValue? =
	typedExprOrNull(parameter(typedValue.type))
		?.let { typedExpr ->
			typedExpr.expr.eval(typedValue.value).let { evaledValue ->
				evaledValue of typedExpr.type
			}
		}
