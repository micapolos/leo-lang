package leo32.runtime

data class TermHasTerm(
	val lhs: Term,
	val rhs: Term)

infix fun Term.has(term: Term) =
	TermHasTerm(this, term)