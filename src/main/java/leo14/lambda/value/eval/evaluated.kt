package leo14.lambda.value.eval

import leo14.lambda.value.Value

sealed class Evaluated

data class ValueEvaluated(val value: Value) : Evaluated()
data class FunctionEvaluated(val function: Function) : Evaluated()

fun evaluated(value: Value): Evaluated = ValueEvaluated(value)
fun evaluated(function: Function): Evaluated = FunctionEvaluated(function)

val Evaluated.function get() = (this as FunctionEvaluated).function
val Evaluated.value get() = (this as ValueEvaluated).value