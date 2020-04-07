package leo14.untyped.typed

import leo14.lambda.runtime.Value

data class Selected(val isRhs: Boolean, val value: Value)

infix fun Boolean.rhsSelected(value: Value) = Selected(this, value)