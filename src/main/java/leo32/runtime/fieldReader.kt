package leo32.runtime

import leo.base.Empty

data class FieldReader(
	val term: Term)

val Term.fieldReader
	get() =
		FieldReader(this)

val Empty.fieldReader
	get() =
		term.fieldReader

fun FieldReader.plus(field: TermField) =
	term.plusResolved(field).fieldReader
