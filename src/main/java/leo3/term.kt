package leo3

data class Term(
	val lhs: Term?,
	val word: Word,
	val rhs: Term?)

fun term(lhs: Term?, word: Word, rhs: Term?) =
	Term(lhs, word, rhs)