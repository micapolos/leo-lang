package leo32.interpreter

import leo.base.fold
import leo32.base.List
import leo32.base.list
import leo32.base.seq
import leo32.runtime.Term
import leo32.runtime.plus
import leo32.runtime.term

data class Either(
	val fieldList: List<EitherField>)

fun either(vararg fields: EitherField) =
	Either(list(*fields))

fun either(name: String) =
	either(name to kind())

val Either.term: Term get() =
	term().fold(fieldList.seq) { plus(it.termField) }