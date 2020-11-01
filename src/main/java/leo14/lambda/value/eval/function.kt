package leo14.lambda.value.eval

import leo14.lambda.Term
import leo14.lambda.invoke
import leo14.lambda.value.Value

data class Function(val scope: Scope, val bodyTerm: Term<Value>)

fun Function.apply(param: Evaluated) = scope.push(param).evaluate(bodyTerm)
fun Scope.function(term: Term<Value>) = Function(this, term)

val Function.term: Term<Value>
	get() =
		scope.evaluatedList.reversed().fold(bodyTerm) { term, evaluated ->
			term.invoke(evaluated.term)
		}
