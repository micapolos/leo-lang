package leo13.lambda.eval

import leo13.push

data class Function(val stack: Stack, val value: Value)

fun function(stack: Stack, value: Value) = Function(stack, value)
operator fun Function.invoke(param: Any) = value.eval(stack.push(param))