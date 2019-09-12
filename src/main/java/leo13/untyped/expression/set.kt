package leo13.untyped.expression

import leo13.untyped.evaluator.ValueLine

data class Set(val line: ValueLine)

fun set(line: ValueLine) = Set(line)
