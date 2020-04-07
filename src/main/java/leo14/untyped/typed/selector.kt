package leo14.untyped.typed

import leo14.lambda.runtime.Value

data class Selector(val selectsLhs: Boolean, val value: Value)

infix fun Boolean.selectsLhs(value: Value) = Selector(this, value)