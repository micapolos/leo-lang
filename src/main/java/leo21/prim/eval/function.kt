package leo21.prim.eval

import leo14.lambda.Term
import leo14.lambda.invoke
import leo21.prim.Prim

data class Function(val scope: Scope, val bodyTerm: Term<Prim>)

fun Function.apply(param: Evaluated) = scope.push(param).evaluate(bodyTerm)
fun Scope.function(term: Term<Prim>) = Function(this, term)

val Function.term: Term<Prim>
	get() =
		scope.evaluatedList.reversed().fold(bodyTerm) { term, evaluated ->
			term.invoke(evaluated.term)
		}
