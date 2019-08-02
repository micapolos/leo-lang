package leo13

import leo9.Stack
import leo9.mapFirst
import leo9.push
import leo9.stack

data class Scope(val functionStack: Stack<Function>)

fun Scope.plus(function: Function) = Scope(functionStack.push(function))
fun scope(vararg functions: Function) = Scope(stack(*functions))

fun Scope.functionBodyOrNull(parameter: PatternParameter) =
	functionStack.mapFirst { bodyOrNull(parameter) }
