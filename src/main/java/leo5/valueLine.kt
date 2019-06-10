package leo5

import leo.base.failIfOr

data class ValueLine(val name: String, val value: Value)

fun line(name: String, value: Value) = ValueLine(name, value)
infix fun String.lineTo(value: Value) = line(this, value)

fun ValueLine.value(name: String) = failIfOr(this.name != name) { value }
val ValueLine.onlyName get() = value.script.empty.run { name }

fun Appendable.append(line: ValueLine): Appendable = append(line.name).append('(').append(line.value).append(')')

fun valueLine(int: Int) = "int" lineTo value(int.toString() lineTo value())
