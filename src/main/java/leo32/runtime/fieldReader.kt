package leo32.runtime

import leo.base.Empty
import leo.base.ifTrue
import leo.base.orElse

data class FieldReader(
	val term: Term)

val Term.fieldReader
	get() =
		FieldReader(this)

val Empty.fieldReader
	get() =
		term.fieldReader

fun FieldReader.plus(field: Field) =
	field.value.isEmpty
		.ifTrue { term.clear.plusResolved(field.name to term) }
		.orElse { term.plusResolved(field) }
		.fieldReader

val FieldReader.quote
	get() =
		term.quote.fieldReader

val FieldReader.shortQuote
	get() =
		term.shortQuote.fieldReader

val FieldReader.unquote
	get() =
		term.unquote.fieldReader

val FieldReader.begin
	get() =
		term.begin.fieldReader
