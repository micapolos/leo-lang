package leo32.interpreter

import leo.base.fold
import leo32.base.List
import leo32.base.add
import leo32.base.list
import leo32.base.seq
import leo32.runtime.Term
import leo32.runtime.fieldSeq
import leo32.runtime.plus
import leo32.runtime.term

data class Either(
	val fieldList: List<EitherField>)

fun either(vararg fields: EitherField) =
	Either(list(*fields))

fun either(name: String) =
	either(name to type())

fun Either.plus(field: EitherField) =
	copy(fieldList = fieldList.add(field))

val Either.term: Term get() =
	term().fold(fieldList.seq) { plus(it.termField) }

val Term.parseEither: Either get() =
	either().fold(fieldSeq) { plus(it.parseEitherField) }

val Term.rawEither: Either get() =
	either().fold(fieldSeq) { plus(it.rawEitherField) }
