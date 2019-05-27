package leo4

data class Ap(val term: Term, val line: Line)

fun ap(term: Term, line: Line) = Ap(term, line)