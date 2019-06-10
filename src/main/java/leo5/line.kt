package leo5

data class Line(val name: String, val value: Value)

fun line(name: String, value: Value = value()) = Line(name, value)
fun Appendable.append(line: Line): Appendable = append(line.name).append('(').append(line.value).append(')')
