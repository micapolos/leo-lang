package leo13

import leo9.Stack
import leo9.mapFirst
import leo9.push
import leo9.stack

data class Functions(val functionStack: Stack<Function>)

val Stack<Function>.functions get() = Functions(this)
fun Functions.plus(function: Function) = functionStack.push(function).functions
fun functions(vararg functions: Function) = stack(*functions).functions

fun Functions.typedExprOrNull(parameter: TypeParameter) =
	functionStack.mapFirst { typedExprOrNull(parameter) }

fun Functions.eval(typedScript: TypedScript): TypedScript =
	evalOrNull(typedScript) ?: typedScript

fun Functions.evalOrNull(typedScript: TypedScript): TypedScript? =
	typedExprOrNull(parameter(typedScript.type))
		?.let { typedExpr ->
			typedExpr.expr.eval(bindings(typedScript.value)).let { evaledValue ->
				typedExpr.type.script(evaledValue) of typedExpr.type
			}
		}

fun Functions.eval(typedValue: TypedValue): TypedValue =
	evalOrNull(typedValue) ?: typedValue

fun Functions.evalOrNull(typedValue: TypedValue): TypedValue? =
	typedExprOrNull(parameter(typedValue.type))
		?.let { typedExpr ->
			typedExpr.expr.eval(bindings(typedValue.value)).let { evaledValue ->
				evaledValue of typedExpr.type
			}
		}
