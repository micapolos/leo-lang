package leo32.base

sealed class Term

data class ApTerm(
	val ap: Ap<Term, Term>): Term()

data class StringTerm(
	val string: String): Term()

val String.term get() =
	StringTerm(this) as Term

val Ap<Term, Term>.term get() =
	ApTerm(this) as Term

fun Term.plus(term: Term) =
	ap(term).term
