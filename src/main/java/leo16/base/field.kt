package leo16.base

data class Field<Value>(val name: String, val value: Value)

infix fun <Value> String.fieldTo(value: Value) = Field(this, value)