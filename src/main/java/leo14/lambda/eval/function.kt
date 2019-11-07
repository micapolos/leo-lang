package leo14.lambda.eval

import leo13.push

data class Function(val stack: Stack, val term: Term)

fun function(stack: Stack, term: Term) = Function(stack, term)
operator fun Function.invoke(param: Any): Any = term.eval(stack.push(param))