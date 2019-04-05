package leo32.interpreter

import leo.base.fold
import leo32.base.List
import leo32.base.isEmpty
import leo32.base.list
import leo32.base.seq
import leo32.runtime.Term
import leo32.runtime.plus
import leo32.runtime.term
import leo32.runtime.to

data class Kind(
	val firstEither: Either,
	val remainingEitherList: List<Either>)

fun kind(either: Either, vararg eithers: Either) =
	Kind(either, list(*eithers))

fun kind(vararg fields: EitherField) =
	kind(either(*fields))

fun kind(name: String, vararg names: String) =
	kind(name to kind().fold(names.reversed()) { kind(it to this) })

val Kind.term get(): Term =
	if (remainingEitherList.isEmpty) firstEither.term
  else term("either" to firstEither.term).fold(remainingEitherList.seq) { plus("either" to it.term) }