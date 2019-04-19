package leo32.runtime

import leo.base.Empty
import leo.base._else
import leo.base._if
import leo.base._then

data class FieldReader(
	val term: Term)

val Term.fieldReader
	get() =
		FieldReader(this)

val Empty.fieldReader
	get() =
		term.fieldReader

fun FieldReader.plus(field: TermField) =
	_if(field.value.isEmpty)
		._then { term.clear.plusResolved(field.name to term) }
		._else { term.plusResolved(field) }
		.fieldReader

val FieldReader.quote
	get() =
		term.quote.fieldReader

val FieldReader.unquote
	get() =
		term.unquote.fieldReader

val FieldReader.begin
	get() =
		term.begin.fieldReader
