package leo5

data class ValueLine(val name: String, val value: Value)

fun line(name: String, value: Value) = ValueLine(name, value)
infix fun String.lineTo(value: Value) = line(this, value)

fun Appendable.append(line: ValueLine): Appendable = append(line.name).append('(').append(line.value).append(')')
