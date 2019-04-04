package leo32.runtime

import leo.base.The
import leo.base.the
import leo32.base.onlyOrNull

data class Script(
	val lhs: Term,
	val field: TermField)

val Script.term get() =
	lhs.plus(field)

val Script.evalGet: Term get() =
	lhs.onlyOrNullAt(field.name)?.let { accessedTerm ->
		if (field.value.isEmpty) accessedTerm
		else field.value.fieldList.onlyOrNull?.let { accessedField ->
			accessedTerm.plus(accessedField).evalGet
		}
	}?:term


val Script.evalWrap: Term get() =
	if (field.value.isEmpty) term(field.name to lhs)
	else term