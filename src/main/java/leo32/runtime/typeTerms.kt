@file:Suppress("unused")

package leo32.runtime

import leo.base.Empty
import leo.base.empty
import leo.base.fold
import leo32.base.*

data class TypeTerms(
	val typeTermTree: Tree<Term?>)

val Tree<Term?>.typeTerms get() =
	TypeTerms(this)

val Empty.typeTerms get() =
	tree<Term>().typeTerms

fun TypeTerms.put(term: Term, type: Term) =
	copy(typeTermTree = typeTermTree.put(term.bitSeq, type))

fun TypeTerms.typeTerm(term: Term): Term =
	plus(term).typeOrNull ?: term

fun TypeTerms.resolveValue(field: TermField) =
	field.map { typeTerm(this) }

fun TypeTerms.plus(field: TermField): TypeTerms =
	typeTermTree
		.at(field.bitSeq)
		?.typeTerms
		?:empty.typeTerms

fun TypeTerms.plus(term: Term): TypeTerms =
	fold(term.fieldSeq) { plus(it) }

val TypeTerms.typeOrNull get() =
	typeTermTree.leafOrNull?.value
