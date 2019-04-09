package leo32.runtime

import leo.base.fold
import leo.base.ifOrNull
import leo32.base.onlyOrNull

data class Node(
	val lhs: Term,
	val field: TermField)

val Node.term get() =
	lhs.plus(field)

val Node.evalGet: Term? get() =
	lhs.onlyOrNullAt(field.name)?.let { accessedTerm ->
		if (field.value.isEmpty) accessedTerm
		else field.value.fieldList.onlyOrNull?.let { accessedField ->
			accessedTerm.plus(accessedField).evalGet
		}
	}

val Node.evalWrap: Term? get() =
	if (!lhs.isEmpty && field.value.isEmpty) term(field.name to lhs)
	else null

val Node.evalEquals: Term? get() =
	if (field.name == "equals") lhs.clear.plus(termField(lhs == field.value))
	else null

val Node.evalClassify: Term? get() =
	ifOrNull(field == "classify" to term()) {
		lhs.listTermSeqOrNull("either")?.let { termSeq ->
			lhs.clear.copy(
				globalScope = lhs.globalScope.fold(termSeq) {
					this
						.defineType(it, lhs)
						.defineTemplate(term("class" to it), template(lhs))
				})
		}
	}

val Node.evalGives: Term? get() =
	ifOrNull(field.name == "gives" && !lhs.isEmpty && !field.value.isEmpty) {
		lhs.clear.copy(globalScope = lhs.globalScope.defineTemplate(lhs, template(field.value)))
	} // TODO: return "error" in case of parse error

val Node.evalUnquote: Term? get() =
	if (field.name == "unquote") lhs.clear.plus(termField(lhs == field.value))
	else null

// TODO: This is incomplete!!!
val Node.evalIs: Term? get() =
	if (field.name == "is") lhs.clear.plus(lhs.leafPlus(field.value))
	else null

val Node.simpleNameOrNull get() =
	if (lhs.isEmpty) field.simpleNameOrNull
	else null

fun Node.simpleAtOrNull(name: String) =
	if (lhs.isEmpty) field.atOrNull(name)
	else null

fun Node.plus(field: TermField) =
	Node(term, field)

fun Node.plus(term: Term) =
	fold(term.fieldSeq) { plus(it) }

fun Node.leafPlus(term: Term) =
	copy(field = field.leafPlus(term))