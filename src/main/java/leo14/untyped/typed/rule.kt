package leo14.untyped.typed

import leo14.lambda.runtime.Value

data class Rule(val fromType: Value, val toType: Value, val valueFn: ValueFn)

fun rule(fromType: Value, toType: Value, valueFn: ValueFn) = Rule(fromType, toType, valueFn)