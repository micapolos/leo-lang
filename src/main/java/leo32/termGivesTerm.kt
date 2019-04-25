package leo32

data class TermGivesTerm(
	val lhs: Term,
	val rhs: Term)

infix fun Term.gives(term: Term) =
	TermGivesTerm(this, term)