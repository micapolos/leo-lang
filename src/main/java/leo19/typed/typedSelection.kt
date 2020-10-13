package leo19.typed

import leo19.term.Term

data class Selection(
	val term: Term,
	val index: Int
)

fun Term.at(index: Int) = Selection(this, index)