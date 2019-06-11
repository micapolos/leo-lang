package leo5

data class Case(val name: Name, val type: Type)

infix fun Name.caseTo(type: Type) = Case(this, type)
fun Case.contains(line: ValueLine) = name.string == line.name && type.contains(line.value)