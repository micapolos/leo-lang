package leo21.type

data class Field(val name: String, val rhs: Type)

infix fun String.fieldTo(rhs: Type) = Field(this, rhs)
