package leo14.untyped.typed

import leo14.lambda.runtime.Value
import leo14.untyped.Thunk

data class Compiled(val type: Thunk, val valueFn: () -> Value)

fun compiled(type: Thunk, valueFn: () -> Value) = Compiled(type, valueFn)
val Compiled.value get() = valueFn()