package leo13.untyped.evaluator

import leo13.untyped.Pattern

data class ValueBinding(val key: Pattern, val value: Value)