package leo13

import leo9.Stack
import leo9.push
import leo9.stack

data class Scope(val functionStack: Stack<Function>)

val Stack<Function>.scope get() = Scope(this)
fun Scope.plus(function: Function) = functionStack.push(function).scope
fun scope(vararg functions: Function) = stack(*functions).scope
