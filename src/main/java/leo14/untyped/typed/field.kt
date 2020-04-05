package leo14.untyped.typed

import leo14.lambda.runtime.Value

data class Field(val name: String, val rhs: Value)

infix fun String.fieldTo(rhs: Value) = Field(this, rhs)