package leo13.lambda.eval

import leo13.push

data class Function(val stack: Stack, val expr: Expr)

fun function(stack: Stack, expr: Expr) = Function(stack, expr)
operator fun Function.invoke(param: Any) = expr.eval(stack.push(param))