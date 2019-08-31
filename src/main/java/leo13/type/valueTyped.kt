package leo13.type

import leo13.value.Value

data class ValueTyped(val value: Value, val pattern: Pattern)

fun typed(value: Value, pattern: Pattern) = ValueTyped(value, pattern)
