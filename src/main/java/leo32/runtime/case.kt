package leo32.runtime

data class Case(
	val key: Term,
	val value: Term)

infix fun Term.caseTo(term: Term) =
	Case(this, term)