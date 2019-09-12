package leo13.untyped.evaluator

import leo13.untyped.Pattern
import leo13.untyped.value.Value

data class Evaluated(val value: Value, val pattern: Pattern)

