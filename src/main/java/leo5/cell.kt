package leo5

data class Cell(val type: Type)

fun cell(type: Type) = Cell(type)
fun Cell.contains(line: ValueLine) = line.name == "cell" && type.contains(line.value)