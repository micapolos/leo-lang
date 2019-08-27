package leo13.type.match

import leo13.type.pattern.Pattern
import leo13.value.Value

data class Match(val value: Value, val pattern: Pattern)

fun match(value: Value, pattern: Pattern) = Match(value, pattern)
