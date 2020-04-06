package leo14.untyped.typed

import leo14.lambda.runtime.Value

data class Untyped(val value: Value)

fun untyped(value: Value) = Untyped(value)