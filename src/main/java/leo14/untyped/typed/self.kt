package leo14.untyped.typed

import leo14.lambda.runtime.Value

data class Self(val value: Value)

fun self(value: Value) = Self(value)