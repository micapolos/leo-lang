package leo32

data class TermHasTerm(
	val lhs: Term,
	val rhs: Term)

infix fun Term.has(term: Term) =
	TermHasTerm(this, term)