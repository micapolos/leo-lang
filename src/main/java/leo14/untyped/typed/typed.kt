package leo14.untyped.typed

import leo14.lambda.runtime.Value

data class Typed(val type: Type, val value: Value)

infix fun Type.typed(value: Value) = Typed(this, value)
val Type.staticTyped: Typed get() = typed(null)
