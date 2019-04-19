package leo32.runtime

import leo.base.fold
import leo.base.ifOrNull

data class Node(
	val lhs: Term,
	val field: TermField)

val Node.term get() =
	lhs.plus(field)

val Node.simpleNameOrNull get() =
	if (lhs.isEmpty) field.simpleNameOrNull
	else null

fun Node.simpleAtOrNull(symbol: Symbol) =
	ifOrNull(lhs.isEmpty) {
		field.atOrNull(symbol)
	}

fun Node.plus(field: TermField) =
	Node(term, field)

fun Node.plus(term: Term) =
	fold(term.fieldSeq) { plus(it) }

fun Node.leafPlus(term: Term) =
	copy(field = field.leafPlus(term))