package leo13

data class ValueLink(val lhs: Value, val line: ValueLine)

fun link(lhs: Value, line: ValueLine) = ValueLink(lhs, line)
