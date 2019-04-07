package leo32.runtime

import leo32.base.onlyOrNull

data class Node(
	val lhs: Term,
	val field: TermField)

val Node.term get() =
	lhs.plus(field)

val Node.evalGet: Term get() =
	lhs.onlyOrNullAt(field.name)?.let { accessedTerm ->
		if (field.value.isEmpty) accessedTerm
		else field.value.fieldList.onlyOrNull?.let { accessedField ->
			accessedTerm.plus(accessedField).evalGet
		}
	}?:term


val Node.evalWrap: Term get() =
	if (field.value.isEmpty) term(field.name to lhs)
	else term
