package leo14.lambda.value

import leo14.lambda.Term

data class Function(val scope: Scope, val term: Term<Value>)

fun Function.apply(value: Value) = scope.push(value).eval(term)