package leo13.type

import leo13.value.Value

data class ValueTyped(val value: Value, val type: Type)

fun typed(value: Value, type: Type) = ValueTyped(value, type)
