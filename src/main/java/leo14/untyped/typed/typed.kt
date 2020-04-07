package leo14.untyped.typed

import leo14.lambda.runtime.Value

data class Typed(val type: Type, val value: Value)

fun Type.with(value: Value) = Typed(this, value)
