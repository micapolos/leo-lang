package leo21.prim.eval

import leo14.lambda.Term
import leo14.lambda.term
import leo21.prim.Prim

sealed class Evaluated

data class ValueEvaluated(val aPrim: Prim) : Evaluated()
data class FunctionEvaluated(val function: Function) : Evaluated()

fun evaluated(aPrim: Prim): Evaluated = ValueEvaluated(aPrim)
fun evaluated(function: Function): Evaluated = FunctionEvaluated(function)

val Evaluated.function get() = (this as FunctionEvaluated).function
val Evaluated.value get() = (this as ValueEvaluated).aPrim

val Evaluated.term: Term<Prim>
	get() =
		when (this) {
			is ValueEvaluated -> term(aPrim)
			is FunctionEvaluated -> function.term
		}