package leo13.untyped.evaluator

import leo13.untyped.Pattern
import leo13.untyped.value.Value

data class ValueBinding(val key: Pattern, val value: Value)