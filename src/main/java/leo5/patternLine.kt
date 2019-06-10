package leo5

data class PatternLine(val name: String, val pattern: Pattern)

fun line(name: String, pattern: Pattern) = PatternLine(name, pattern)