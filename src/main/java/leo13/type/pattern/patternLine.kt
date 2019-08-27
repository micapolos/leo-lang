package leo13.type.pattern

data class PatternLine(val name: String, val rhs: Pattern)

infix fun String.lineTo(rhs: Pattern) = PatternLine(this, rhs)

fun PatternLine.contains(line: PatternLine): Boolean =
	name == line.name && rhs.contains(line.rhs)
