package leo32.runtime

import leo.base.fold
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

val Node.evalQuote: Term? get() =
	if (field.name == "quote") lhs.fold(field.value.fieldSeq) { plus(it) }
	else null

val Node.evalEquals: Term? get() =
	if (field.name == "equals") lhs.clear.plus(termField(lhs == field.value))
	else null