package leo5

data class Field(val name: Name, val type: Type)

infix fun Name.of(value: Type) = Field(this, value)
fun Field.contains(line: ValueLine) = name.string == line.name && type.contains(line.value)