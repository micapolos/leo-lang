package leo13.type.pattern

data class PatternLink(val lhs: Pattern, val line: PatternLine)

fun link(lhs: Pattern, line: PatternLine) = PatternLink(lhs, line)

fun PatternLink.contains(link: PatternLink): Boolean =
	lhs.contains(link.lhs) && line.contains(link.line)
