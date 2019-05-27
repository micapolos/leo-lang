package leo4

import leo.base.fold

sealed class Term
data class LineTerm(val line: Line) : Term()
data class ApTerm(val ap: Ap) : Term()

fun term(line: Line): Term = LineTerm(line)
fun term(ap: Ap): Term = ApTerm(ap)
fun term(line: Line, vararg lines: Line) = term(line).fold(lines) { term(ap(this, it)) }

