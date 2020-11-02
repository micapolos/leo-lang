package leo21.value.eval

import leo14.lambda.Term
import leo14.lambda.term
import leo21.value.Value

sealed class Evaluated

data class ValueEvaluated(val value: Value) : Evaluated()
data class FunctionEvaluated(val function: Function) : Evaluated()

fun evaluated(value: Value): Evaluated = ValueEvaluated(value)
fun evaluated(function: Function): Evaluated = FunctionEvaluated(function)

val Evaluated.function get() = (this as FunctionEvaluated).function
val Evaluated.value get() = (this as ValueEvaluated).value

val Evaluated.term: Term<Value>
	get() =
		when (this) {
			is ValueEvaluated -> term(value)
			is FunctionEvaluated -> function.term
		}