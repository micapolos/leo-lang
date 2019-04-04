package leo32.runtime

data class Case(
	val key: Term,
	val value: Term)

infix fun Term.gives(term: Term) =
	Case(this, term)