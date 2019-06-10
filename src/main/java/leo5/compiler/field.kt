package leo5.compiler

data class Field(val offset: Offset)

fun field(offset: Offset) = Field(offset)
