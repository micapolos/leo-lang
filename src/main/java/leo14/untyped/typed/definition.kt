package leo14.untyped.typed

import leo.base.ifOrNull
import leo14.lambda.runtime.Value

data class Definition(val type: Value, val value: Value)

fun Definition.atType(t: Value): Value = ifOrNull(type == t) { value }