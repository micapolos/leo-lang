package leo14.lambda.value.eval

import leo14.lambda.Term
import leo14.lambda.value.Value

data class Function(val scope: Scope, val term: Term<Value>)

fun Function.apply(param: Evaluated) = scope.push(param).evaluate(term)
fun Scope.function(term: Term<Value>) = Function(this, term)