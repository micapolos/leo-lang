package leo13.type.match

data class MatchLine(val name: String, val rhs: Match)

infix fun String.lineTo(rhs: Match) = MatchLine(this, rhs)
