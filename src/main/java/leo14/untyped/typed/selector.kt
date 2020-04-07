package leo14.untyped.typed

import leo14.lambda.runtime.Value

data class Selected(val isLhs: Boolean, val value: Value)

infix fun Boolean.lhsSelected(value: Value) = Selected(this, value)