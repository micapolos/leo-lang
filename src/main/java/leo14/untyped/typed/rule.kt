package leo14.untyped.typed

import leo14.lambda.runtime.Value

data class Rule(val fromType: Type, val toType: Type, val valueFn: (Value) -> Value)

fun rule(fromType: Type, toType: Type, fn: (Value) -> Value) = Rule(fromType, toType, fn)
