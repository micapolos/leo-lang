package leo13.untyped.expression

import leo13.untyped.evaluator.Value

data class Constant(val value: Value)

fun constant(value: Value) = Constant(value)
