package leo32

import leo.base.fold
import leo.base.ifOrNull
import leo.base.notNullIf

data class Node(
	val lhs: Term,
	val field: Field)

val Node.term get() =
	lhs.plus(field)

val Node.simpleFieldOrNull
	get() =
		notNullIf(lhs.isEmpty) { field }

val Node.simpleNameOrNull get() =
	simpleFieldOrNull?.simpleNameOrNull

val Node.isSimple
	get() =
		lhs.isEmpty

fun Node.simpleAtOrNull(symbol: Symbol) =
	ifOrNull(lhs.isEmpty) {
		field.atOrNull(symbol)
	}

fun Node.plus(field: Field) =
	Node(term, field)

fun Node.plus(term: Term) =
	fold(term.fieldSeq) { plus(it) }

fun Node.leafPlus(term: Term) =
	copy(field = field.leafPlus(term))